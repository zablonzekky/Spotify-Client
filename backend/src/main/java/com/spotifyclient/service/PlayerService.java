package com.spotifyclient.service;

import com.spotifyclient.spotify.SpotifyApiClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerService {

    private final SpotifyApiClient spotifyApiClient;

    public PlayerService(SpotifyApiClient spotifyApiClient) {
        this.spotifyApiClient = spotifyApiClient;
    }

    public Map<String, Object> getPlaybackState(String accessToken) {
        return spotifyApiClient.get("/me/player", accessToken, Map.of());
    }

    public Map<String, Object> getCurrentlyPlaying(String accessToken) {
        return spotifyApiClient.get("/me/player/currently-playing", accessToken, Map.of());
    }

    public Map<String, Object> getAvailableDevices(String accessToken) {
        return spotifyApiClient.get("/me/player/devices", accessToken, Map.of());
    }

    public void transferPlayback(String accessToken, String deviceId, boolean play) {
        spotifyApiClient.put("/me/player", accessToken, Map.of(
                "device_ids", List.of(deviceId),
                "play", play
        ));
    }

    public void play(String accessToken, String deviceId, String contextUri,
                     List<String> uris, Integer offsetPosition, Integer positionMs) {
        Map<String, Object> body = new HashMap<>();
        if (contextUri    != null) body.put("context_uri", contextUri);
        if (uris          != null) body.put("uris", uris);
        if (offsetPosition != null) body.put("offset", Map.of("position", offsetPosition));
        if (positionMs    != null) body.put("position_ms", positionMs);

        String path = deviceId != null
                ? "/me/player/play?device_id=" + deviceId
                : "/me/player/play";
        spotifyApiClient.put(path, accessToken, body);
    }

    public void pause(String accessToken, String deviceId) {
        String path = deviceId != null
                ? "/me/player/pause?device_id=" + deviceId
                : "/me/player/pause";
        spotifyApiClient.put(path, accessToken, Map.of());
    }

    public void skipToNext(String accessToken, String deviceId) {
        String path = deviceId != null
                ? "/me/player/next?device_id=" + deviceId
                : "/me/player/next";
        spotifyApiClient.post(path, accessToken, Map.of());
    }

    public void skipToPrevious(String accessToken, String deviceId) {
        String path = deviceId != null
                ? "/me/player/previous?device_id=" + deviceId
                : "/me/player/previous";
        spotifyApiClient.post(path, accessToken, Map.of());
    }

    public void seek(String accessToken, int positionMs, String deviceId) {
        String path = "/me/player/seek?position_ms=" + positionMs
                + (deviceId != null ? "&device_id=" + deviceId : "");
        spotifyApiClient.put(path, accessToken, Map.of());
    }

    public void setVolume(String accessToken, int volumePercent, String deviceId) {
        String path = "/me/player/volume?volume_percent=" + volumePercent
                + (deviceId != null ? "&device_id=" + deviceId : "");
        spotifyApiClient.put(path, accessToken, Map.of());
    }

    public void setShuffle(String accessToken, boolean state, String deviceId) {
        String path = "/me/player/shuffle?state=" + state
                + (deviceId != null ? "&device_id=" + deviceId : "");
        spotifyApiClient.put(path, accessToken, Map.of());
    }

    public void setRepeat(String accessToken, String state, String deviceId) {
        // state: "track" | "context" | "off"
        String path = "/me/player/repeat?state=" + state
                + (deviceId != null ? "&device_id=" + deviceId : "");
        spotifyApiClient.put(path, accessToken, Map.of());
    }

    public void addToQueue(String accessToken, String uri, String deviceId) {
        String path = "/me/player/queue?uri=" + uri
                + (deviceId != null ? "&device_id=" + deviceId : "");
        spotifyApiClient.post(path, accessToken, Map.of());
    }

    public Map<String, Object> getQueue(String accessToken) {
        return spotifyApiClient.get("/me/player/queue", accessToken, Map.of());
    }
}
