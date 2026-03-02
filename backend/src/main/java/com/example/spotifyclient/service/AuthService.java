package com.example.spotifyclient.service;

import com.example.spotifyclient.client.SpotifyHttpClient;
import com.example.spotifyclient.config.SpotifyProperties;
import com.example.spotifyclient.dto.SpotifyTokenResponse;
import com.example.spotifyclient.exception.UnauthorizedException;
import com.example.spotifyclient.model.SpotifyToken;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final SpotifyProperties properties;
    private final SpotifyHttpClient spotifyHttpClient;
    private final TokenStore tokenStore;

    public AuthService(SpotifyProperties properties, SpotifyHttpClient spotifyHttpClient, TokenStore tokenStore) {
        this.properties = properties;
        this.spotifyHttpClient = spotifyHttpClient;
        this.tokenStore = tokenStore;
    }

    public String createAuthorizationUrl(String state) {
        return UriComponentsBuilder.fromHttpUrl(properties.accountsBaseUrl() + "/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", properties.clientId())
                .queryParam("scope", properties.scopes())
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    public String exchangeCode(String code, String existingSessionId) {
        SpotifyTokenResponse response = spotifyHttpClient.exchangeCode(code);

        String sessionId = Optional.ofNullable(existingSessionId).orElse(UUID.randomUUID().toString());
        tokenStore.save(sessionId, new SpotifyToken(
                response.accessToken(),
                response.refreshToken(),
                Instant.now().plusSeconds(response.expiresIn())
        ));
        return sessionId;
    }

    public String getValidAccessToken(String sessionId) {
        SpotifyToken token = tokenStore.get(sessionId)
                .orElseThrow(() -> new UnauthorizedException("Not authenticated. Please sign in with Spotify."));

        if (!token.isExpired()) {
            return token.accessToken();
        }

        SpotifyTokenResponse refreshResponse = spotifyHttpClient.refreshToken(token.refreshToken());
        SpotifyToken refreshed = new SpotifyToken(
                refreshResponse.accessToken(),
                Optional.ofNullable(refreshResponse.refreshToken()).orElse(token.refreshToken()),
                Instant.now().plusSeconds(refreshResponse.expiresIn())
        );
        tokenStore.save(sessionId, refreshed);
        return refreshed.accessToken();
    }
}
