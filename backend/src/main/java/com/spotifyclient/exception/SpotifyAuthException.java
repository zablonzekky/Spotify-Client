package com.spotifyclient.exception;

public class SpotifyAuthException extends RuntimeException {
    public SpotifyAuthException(String message) {
        super(message);
    }
}