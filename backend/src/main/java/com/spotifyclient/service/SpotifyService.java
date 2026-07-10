package com.spotifyclient.service;

import com.spotifyclient.dto.SearchRequest;
import com.spotifyclient.spotify.SpotifyApiClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpotifyService {

    private final SpotifyApiClient spotifyApiClient;

    public SpotifyService(SpotifyApiClient spotifyApiClient) {
        this.spotifyApiClient = spotifyApiClient;
    }

    // ── Browse ──────────────────────────────────────────────────────────────

    /**
     * Cache key does NOT include the access token —
     * featured playlists are the same for all users.
     */
    @Cacheable(value = "featured", key = "#locale + '_' + #limit")
    public Map<String, Object> getFeaturedPlaylists(String accessToken, String locale, Integer limit) {
        return spotifyApiClient.get("/browse/featured-playlists", accessToken, Map.of(
                "locale", locale,
                "limit", limit == null ? 12 : limit
        ));
    }

    @Cacheable(value = "new-releases", key = "#market + '_' + #limit")
    public Map<String, Object> getNewReleases(String accessToken, Integer limit, String market) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit == null ? 12 : limit);
        if (market != null) params.put("market", market);
        return spotifyApiClient.get("/browse/new-releases", accessToken, params);
    }

    @Cacheable(value = "categories", key = "#locale + '_' + #limit")
    public Map<String, Object> getCategories(String accessToken, String locale, Integer limit) {
        return spotifyApiClient.get("/browse/categories", accessToken, Map.of(
                "locale", locale == null ? "en_US" : locale,
                "limit",  limit  == null ? 20      : limit
        ));
    }

    public Map<String, Object> getCategoryPlaylists(String accessToken, String categoryId, Integer limit) {
        return spotifyApiClient.get(
                "/browse/categories/" + categoryId + "/playlists",
                accessToken,
                Map.of("limit", limit == null ? 12 : limit)
        );
    }

    // ── Search ───────────────────────────────────────────────────────────────

    public Map<String, Object> search(String accessToken, SearchRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("q",      request.query());
        params.put("type",   request.types()  == null ? "track,artist,album,playlist" : request.types());
        params.put("limit",  request.limit()  == null ? 10  : request.limit());
        params.put("market", request.market() == null ? "US" : request.market());
        if (request.offset() != null) params.put("offset", request.offset());
        return spotifyApiClient.get("/search", accessToken, params);
    }

    // ── Recommendations ──────────────────────────────────────────────────────

    @Cacheable(value = "recommendations", key = "#seedTracks + '_' + #seedArtists + '_' + #seedGenres")
    public Map<String, Object> getRecommendations(
            String accessToken,
            String seedTracks,
            String seedArtists,
            String seedGenres,
            Integer limit,
            String market) {

        Map<String, Object> params = new HashMap<>();
        if (seedTracks  != null) params.put("seed_tracks",  seedTracks);
        if (seedArtists != null) params.put("seed_artists", seedArtists);
        if (seedGenres  != null) params.put("seed_genres",  seedGenres);
        params.put("limit",  limit  == null ? 20  : limit);
        params.put("market", market == null ? "US" : market);
        return spotifyApiClient.get("/recommendations", accessToken, params);
    }

    public Map<String, Object> getRecommendationGenres(String accessToken) {
        return spotifyApiClient.get("/recommendations/available-genre-seeds", accessToken, Map.of());
    }

    // ── Artist ───────────────────────────────────────────────────────────────

    @Cacheable(value = "artist", key = "#artistId")
    public Map<String, Object> getArtist(String accessToken, String artistId) {
        return spotifyApiClient.get("/artists/" + artistId, accessToken, Map.of());
    }

    @Cacheable(value = "artist", key = "#artistId + '_albums'")
    public Map<String, Object> getArtistAlbums(String accessToken, String artistId, Integer limit) {
        return spotifyApiClient.get("/artists/" + artistId + "/albums", accessToken,
                Map.of("limit", limit == null ? 10 : limit));
    }

    @Cacheable(value = "artist", key = "#artistId + '_top'")
    public Map<String, Object> getArtistTopTracks(String accessToken, String artistId, String market) {
        return spotifyApiClient.get("/artists/" + artistId + "/top-tracks", accessToken,
                Map.of("market", market == null ? "US" : market));
    }

    @Cacheable(value = "artist", key = "#artistId + '_related'")
    public Map<String, Object> getRelatedArtists(String accessToken, String artistId) {
        return spotifyApiClient.get("/artists/" + artistId + "/related-artists", accessToken, Map.of());
    }

    // ── Album ────────────────────────────────────────────────────────────────

    @Cacheable(value = "album", key = "#albumId")
    public Map<String, Object> getAlbum(String accessToken, String albumId) {
        return spotifyApiClient.get("/albums/" + albumId, accessToken, Map.of());
    }

    @Cacheable(value = "album", key = "#albumId + '_tracks'")
    public Map<String, Object> getAlbumTracks(String accessToken, String albumId, Integer limit) {
        return spotifyApiClient.get("/albums/" + albumId + "/tracks", accessToken,
                Map.of("limit", limit == null ? 50 : limit));
    }

    // ── Playlist ─────────────────────────────────────────────────────────────

    @Cacheable(value = "playlist", key = "#playlistId")
    public Map<String, Object> getPlaylist(String accessToken, String playlistId) {
        return spotifyApiClient.get("/playlists/" + playlistId, accessToken, Map.of());
    }

    @Cacheable(value = "playlist", key = "#playlistId + '_tracks_' + #offset")
    public Map<String, Object> getPlaylistTracks(String accessToken, String playlistId,
                                                 Integer limit, Integer offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit",  limit  == null ? 50 : limit);
        params.put("offset", offset == null ? 0  : offset);
        return spotifyApiClient.get("/playlists/" + playlistId + "/tracks", accessToken, params);
    }

    // ── Track ────────────────────────────────────────────────────────────────

    public Map<String, Object> getTrack(String accessToken, String trackId) {
        return spotifyApiClient.get("/tracks/" + trackId, accessToken, Map.of());
    }

    public Map<String, Object> getTrackAudioFeatures(String accessToken, String trackId) {
        return spotifyApiClient.get("/audio-features/" + trackId, accessToken, Map.of());
    }

    // ── Cache eviction (every 10 minutes) ────────────────────────────────────

    @Scheduled(fixedRate = 600_000)
    @CacheEvict(value = {"featured", "new-releases", "categories"}, allEntries = true)
    public void evictBrowseCaches() {}
}