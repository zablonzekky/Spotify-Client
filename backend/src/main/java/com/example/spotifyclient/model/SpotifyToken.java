package com.example.spotifyclient.model;

import java.time.Instant;

public record SpotifyToken(
        String accessToken,
        String refreshToken,
        Instant expiresAt
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt.minusSeconds(30));
    }
}
