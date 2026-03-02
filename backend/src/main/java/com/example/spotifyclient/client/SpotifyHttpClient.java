package com.example.spotifyclient.client;

import com.example.spotifyclient.config.SpotifyProperties;
import com.example.spotifyclient.dto.SpotifyTokenResponse;
import com.example.spotifyclient.exception.SpotifyClientException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class SpotifyHttpClient {

    private final RestClient restClient;
    private final SpotifyProperties properties;

    public SpotifyHttpClient(RestClient restClient, SpotifyProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public SpotifyTokenResponse exchangeCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", properties.redirectUri());

        return restClient.post()
                .uri(properties.accountsBaseUrl() + "/api/token")
                .header("Authorization", "Basic " + encodedCredentials())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(SpotifyTokenResponse.class);
    }

    public SpotifyTokenResponse refreshToken(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);

        return restClient.post()
                .uri(properties.accountsBaseUrl() + "/api/token")
                .header("Authorization", "Basic " + encodedCredentials())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(SpotifyTokenResponse.class);
    }

    public Map<String, Object> fetchMe(String accessToken) {
        return request("/me", accessToken);
    }

    public Map<String, Object> search(String accessToken, String query) {
        String uri = UriComponentsBuilder.fromHttpUrl(properties.apiBaseUrl() + "/search")
                .queryParam("q", query)
                .queryParam("type", "track,artist,album,playlist")
                .queryParam("limit", 8)
                .toUriString();

        return restClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(Map.class);
    }

    private Map<String, Object> request(String path, String accessToken) {
        Map<String, Object> response = restClient.get()
                .uri(properties.apiBaseUrl() + path)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(Map.class);

        if (response == null) {
            throw new SpotifyClientException("Spotify returned an empty response");
        }
        return response;
    }

    private String encodedCredentials() {
        String raw = properties.clientId() + ":" + properties.clientSecret();
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }
}
