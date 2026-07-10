package com.spotifyclient.exception;

public class SpotifyRateLimitException extends RuntimeException {
    public SpotifyRateLimitException(String message) {
        super(message);
    }
}