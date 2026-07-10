package com.spotifyclient.service;

import com.spotifyclient.config.SpotifyProperties;
import com.spotifyclient.dto.AuthTokenResponse;
import com.spotifyclient.exception.SpotifyAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final String ACCOUNTS_BASE_URL = "https://accounts.spotify.com";

    private final SpotifyProperties spotifyProperties;
    private final RestClient authRestClient;

    public AuthService(SpotifyProperties spotifyProperties) {
        this.spotifyProperties = spotifyProperties;
        this.authRestClient = RestClient.builder().baseUrl(ACCOUNTS_BASE_URL).build();
    }

    public String buildAuthorizeUrl(String state, String codeChallenge) {
        return UriComponentsBuilder.fromHttpUrl(ACCOUNTS_BASE_URL + "/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", spotifyProperties.clientId())
                .queryParam("scope", spotifyProperties.scopes())
                .queryParam("redirect_uri", spotifyProperties.redirectUri())
                .queryParam("state", state)
                .queryParam("code_challenge_method", "S256")
                .queryParam("code_challenge", codeChallenge)
                .build()
                .toUriString();
    }

    public AuthTokenResponse exchangeCode(String code, String codeVerifier) {
        log.info("Exchanging authorization code for tokens");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", spotifyProperties.redirectUri());
        formData.add("code_verifier", codeVerifier);

        return requestToken(formData);
    }

    public AuthTokenResponse refreshToken(String refreshToken) {
        log.info("Refreshing access token");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);

        return requestToken(formData);
    }

    private AuthTokenResponse requestToken(MultiValueMap<String, String> formData) {
        String credentials = Base64.getEncoder().encodeToString(
                (spotifyProperties.clientId() + ":" + spotifyProperties.clientSecret())
                        .getBytes(StandardCharsets.UTF_8));

        try {
            Map<String, Object> response = authRestClient.post()
                    .uri("/api/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Authorization", "Basic " + credentials)
                    .body(formData)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                throw new SpotifyAuthException("Empty token response from Spotify");
            }

            return new AuthTokenResponse(
                    (String)  response.get("access_token"),
                    (String)  response.get("refresh_token"),
                    (Integer) response.get("expires_in"),
                    (String)  response.get("scope"),
                    (String)  response.get("token_type")
            );
        } catch (HttpClientErrorException e) {
            log.error("Token request failed: {}", e.getStatusCode());
            throw new SpotifyAuthException("Failed to obtain Spotify token: " + e.getMessage());
        }
    }
}