package com.spotifyclient.service;

import com.spotifyclient.dto.SearchRequest;
import com.spotifyclient.spotify.SpotifyApiClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpotifyService {

    private final SpotifyApiClient spotifyApiClient;

    public SpotifyService(SpotifyApiClient spotifyApiClient) {
        this.spotifyApiClient = spotifyApiClient;
    }

    @Cacheable("featured")
    public Map<String, Object> getFeaturedPlaylists(String accessToken, String locale, Integer limit) {
        return spotifyApiClient.get("/browse/featured-playlists", accessToken, Map.of(
                "locale", locale,
                "limit", limit == null ? 12 : limit
        ));
    }

    @Cacheable("new-releases")
    public Map<String, Object> getNewReleases(String accessToken, Integer limit) {
        return spotifyApiClient.get("/browse/new-releases", accessToken, Map.of(
                "limit", limit == null ? 12 : limit
        ));
    }

    public Map<String, Object> search(String accessToken, SearchRequest request) {
        return spotifyApiClient.get("/search", accessToken, Map.of(
                "q", request.query(),
                "type", request.types() == null ? "track,artist,album,playlist" : request.types(),
                "limit", request.limit() == null ? 10 : request.limit(),
                "market", request.market() == null ? "US" : request.market()
        ));
    }
}
