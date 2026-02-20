package com.spotifyclient.spotify;

import com.spotifyclient.exception.SpotifyApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Component
public class SpotifyApiClient {

    private final RestClient spotifyRestClient;

    public SpotifyApiClient(RestClient spotifyRestClient) {
        this.spotifyRestClient = spotifyRestClient;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 600, multiplier = 2.0), retryFor = SpotifyApiException.class)
    public Map<String, Object> get(String path, String accessToken, Map<String, ?> queryParams) {
        try {
            return spotifyRestClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path(path);
                        queryParams.forEach(uriBuilder::queryParam);
                        return uriBuilder.build();
                    })
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        if (res.getStatusCode().value() == 429) {
                            throw new SpotifyApiException("Spotify API rate-limited request");
                        }
                    })
                    .body(Map.class);
        } catch (RestClientResponseException ex) {
            throw new SpotifyApiException("Spotify API request failed with status %s".formatted(ex.getStatusCode()));
        }
    }
}
