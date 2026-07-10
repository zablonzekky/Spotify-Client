package com.spotifyclient.service;

import com.spotifyclient.spotify.SpotifyApiClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LibraryService {

    private final SpotifyApiClient spotifyApiClient;

    public LibraryService(SpotifyApiClient spotifyApiClient) {
        this.spotifyApiClient = spotifyApiClient;
    }

    // ── Saved Tracks ─────────────────────────────────────────────────────────

    public Map<String, Object> getSavedTracks(String accessToken, Integer limit, Integer offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit",  limit  == null ? 20 : limit);
        params.put("offset", offset == null ? 0  : offset);
        return spotifyApiClient.get("/me/tracks", accessToken, params);
    }

    public void saveTrack(String accessToken, String trackId) {
        spotifyApiClient.put("/me/tracks?ids=" + trackId, accessToken, Map.of());
    }

    public void removeTrack(String accessToken, String trackId) {
        spotifyApiClient.delete("/me/tracks?ids=" + trackId, accessToken);
    }

    public Map<String, Object> checkSavedTracks(String accessToken, List<String> trackIds) {
        return spotifyApiClient.get("/me/tracks/contains", accessToken,
                Map.of("ids", String.join(",", trackIds)));
    }

    // ── Saved Albums ─────────────────────────────────────────────────────────

    public Map<String, Object> getSavedAlbums(String accessToken, Integer limit, Integer offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit",  limit  == null ? 20 : limit);
        params.put("offset", offset == null ? 0  : offset);
        return spotifyApiClient.get("/me/albums", accessToken, params);
    }

    public void saveAlbum(String accessToken, String albumId) {
        spotifyApiClient.put("/me/albums?ids=" + albumId, accessToken, Map.of());
    }

    public void removeAlbum(String accessToken, String albumId) {
        spotifyApiClient.delete("/me/albums?ids=" + albumId, accessToken);
    }

    // ── User Playlists ────────────────────────────────────────────────────────

    public Map<String, Object> getUserPlaylists(String accessToken, Integer limit, Integer offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit",  limit  == null ? 20 : limit);
        params.put("offset", offset == null ? 0  : offset);
        return spotifyApiClient.get("/me/playlists", accessToken, params);
    }

    public Map<String, Object> createPlaylist(String accessToken, String userId,
                                               String name, String description, boolean isPublic) {
        return spotifyApiClient.put("/users/" + userId + "/playlists", accessToken, Map.of(
                "name",        name,
                "description", description,
                "public",      isPublic
        ));
    }

    public void addTracksToPlaylist(String accessToken, String playlistId, List<String> uris) {
        spotifyApiClient.post("/playlists/" + playlistId + "/tracks", accessToken,
                Map.of("uris", uris));
    }

    public void removeTracksFromPlaylist(String accessToken, String playlistId, List<String> uris) {
        List<Map<String, String>> tracks = uris.stream()
                .map(uri -> Map.of("uri", uri))
                .toList();
        spotifyApiClient.post("/playlists/" + playlistId + "/tracks/delete", accessToken,
                Map.of("tracks", tracks));
    }

    // ── Followed Artists ─────────────────────────────────────────────────────

    public Map<String, Object> getFollowedArtists(String accessToken, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("type",  "artist");
        params.put("limit", limit == null ? 20 : limit);
        return spotifyApiClient.get("/me/following", accessToken, params);
    }

    public void followArtist(String accessToken, String artistId) {
        spotifyApiClient.put("/me/following?type=artist&ids=" + artistId, accessToken, Map.of());
    }

    public void unfollowArtist(String accessToken, String artistId) {
        spotifyApiClient.delete("/me/following?type=artist&ids=" + artistId, accessToken);
    }

    // ── Recently Played ───────────────────────────────────────────────────────

    public Map<String, Object> getRecentlyPlayed(String accessToken, Integer limit) {
        return spotifyApiClient.get("/me/player/recently-played", accessToken,
                Map.of("limit", limit == null ? 20 : limit));
    }

    public Map<String, Object> getTopItems(String accessToken, String type, String timeRange, Integer limit) {
        return spotifyApiClient.get("/me/top/" + type, accessToken, Map.of(
                "time_range", timeRange == null ? "medium_term" : timeRange,
                "limit",      limit     == null ? 20            : limit
        ));
    }
}
