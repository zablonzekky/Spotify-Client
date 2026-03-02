package com.example.spotifyclient.service;

import com.example.spotifyclient.client.SpotifyHttpClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpotifyService {

    private final AuthService authService;
    private final SpotifyHttpClient spotifyHttpClient;

    public SpotifyService(AuthService authService, SpotifyHttpClient spotifyHttpClient) {
        this.authService = authService;
        this.spotifyHttpClient = spotifyHttpClient;
    }

    public Map<String, Object> me(String sessionId) {
        String accessToken = authService.getValidAccessToken(sessionId);
        return spotifyHttpClient.fetchMe(accessToken);
    }

    public Map<String, Object> search(String sessionId, String query) {
        String accessToken = authService.getValidAccessToken(sessionId);
        return spotifyHttpClient.search(accessToken, query);
    }
}
