package com.spotifyclient.exception;

public class SpotifyApiException extends RuntimeException {

    public SpotifyApiException(String message) {
        super(message);
    }
}
