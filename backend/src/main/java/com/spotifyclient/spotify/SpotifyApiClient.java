package com.spotifyclient.spotify;

import com.spotifyclient.exception.SpotifyApiException;
import com.spotifyclient.exception.SpotifyAuthException;
import com.spotifyclient.exception.SpotifyRateLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class SpotifyApiClient {

    private static final Logger log = LoggerFactory.getLogger(SpotifyApiClient.class);

    private final RestClient restClient;

    public SpotifyApiClient(RestClient spotifyRestClient) {
        this.restClient = spotifyRestClient;
    }

    @Retryable(
            retryFor = SpotifyApiException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2)
    )
    public Map<String, Object> get(String path, String accessToken, Map<String, Object> params) {
        String uri = buildUri(path, params);
        log.debug("Spotify GET {} params={}", path, params.keySet());

        try {
            Map<String, Object> response = restClient.get()
                    .uri(uri)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(Map.class);
            log.debug("Spotify GET {} -> OK", path);
            return response;
        } catch (HttpClientErrorException e) {
            throw mapClientError(e, path);
        } catch (HttpServerErrorException e) {
            log.warn("Spotify server error on GET {}: {}", path, e.getStatusCode());
            throw new SpotifyApiException("Spotify service temporarily unavailable");
        }
    }

    @Retryable(
            retryFor = SpotifyApiException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 300)
    )
    public Map<String, Object> put(String path, String accessToken, Map<String, Object> body) {
        log.debug("Spotify PUT {}", path);

        try {
            Map<String, Object> response = restClient.put()
                    .uri(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(body)
                    .retrieve()
                    .body(Map.class);
            return response != null ? response : Map.of();
        } catch (HttpClientErrorException e) {
            throw mapClientError(e, path);
        } catch (HttpServerErrorException e) {
            log.warn("Spotify server error on PUT {}: {}", path, e.getStatusCode());
            throw new SpotifyApiException("Spotify service temporarily unavailable");
        }
    }

    public void post(String path, String accessToken, Map<String, Object> body) {
        log.debug("Spotify POST {}", path);

        try {
            restClient.post()
                    .uri(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            throw mapClientError(e, path);
        } catch (HttpServerErrorException e) {
            log.warn("Spotify server error on POST {}: {}", path, e.getStatusCode());
            throw new SpotifyApiException("Spotify service temporarily unavailable");
        }
    }

    public void delete(String path, String accessToken) {
        log.debug("Spotify DELETE {}", path);

        try {
            restClient.delete()
                    .uri(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            throw mapClientError(e, path);
        }
    }

    private String buildUri(String path, Map<String, Object> params) {
        if (params == null || params.isEmpty()) return path;

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);
        params.forEach((k, v) -> {
            if (v != null) builder.queryParam(k, v);
        });
        return builder.build().toUriString();
    }

    private RuntimeException mapClientError(HttpClientErrorException e, String path) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        log.warn("Spotify client error on {}: {}", path, status);

        return switch (status) {
            case UNAUTHORIZED -> new SpotifyAuthException("Spotify token is invalid or expired");
            case FORBIDDEN    -> new SpotifyAuthException("Insufficient Spotify permissions");
            case TOO_MANY_REQUESTS -> new SpotifyRateLimitException("Spotify rate limit exceeded");
            case NOT_FOUND    -> new SpotifyApiException("Resource not found: " + path);
            default           -> new SpotifyApiException("Spotify API error: " + e.getMessage());
        };
    }
}