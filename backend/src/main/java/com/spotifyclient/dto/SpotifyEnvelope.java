package com.spotifyclient.dto;

import java.util.Map;

public record SpotifyEnvelope(Map<String, Object> payload) {
}
