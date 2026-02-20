package com.spotifyclient.dto;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        Integer expiresIn,
        String scope,
        String tokenType
) {
}
