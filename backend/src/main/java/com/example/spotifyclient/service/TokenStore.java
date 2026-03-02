package com.example.spotifyclient.service;

import com.example.spotifyclient.model.SpotifyToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {
    private final Map<String, SpotifyToken> tokenBySession = new ConcurrentHashMap<>();

    public void save(String sessionId, SpotifyToken token) {
        tokenBySession.put(sessionId, token);
    }

    public Optional<SpotifyToken> get(String sessionId) {
        return Optional.ofNullable(tokenBySession.get(sessionId));
    }
}
