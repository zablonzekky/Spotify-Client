package com.spotifyclient.controller;

import com.spotifyclient.dto.SearchRequest;
import com.spotifyclient.dto.SpotifyEnvelope;
import com.spotifyclient.service.SpotifyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/spotify")
@Validated
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    // ── Browse ────────────────────────────────────────────────────────────────

    @GetMapping("/featured")
    public SpotifyEnvelope featured(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(defaultValue = "en_US") String locale,
            @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(spotifyService.getFeaturedPlaylists(accessToken, locale, limit));
    }

    @GetMapping("/new-releases")
    public SpotifyEnvelope newReleases(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String market) {
        return new SpotifyEnvelope(spotifyService.getNewReleases(accessToken, limit, market));
    }

    @GetMapping("/categories")
    public SpotifyEnvelope categories(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) String locale,
            @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(spotifyService.getCategories(accessToken, locale, limit));
    }

    @GetMapping("/categories/{categoryId}/playlists")
    public SpotifyEnvelope categoryPlaylists(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String categoryId,
            @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(spotifyService.getCategoryPlaylists(accessToken, categoryId, limit));
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @PostMapping("/search")
    public SpotifyEnvelope search(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @Valid @RequestBody SearchRequest request) {
        return new SpotifyEnvelope(spotifyService.search(accessToken, request));
    }

    // ── Recommendations ───────────────────────────────────────────────────────

    @GetMapping("/recommendations")
    public SpotifyEnvelope recommendations(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) String seedTracks,
            @RequestParam(required = false) String seedArtists,
            @RequestParam(required = false) String seedGenres,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String market) {
        return new SpotifyEnvelope(spotifyService.getRecommendations(
                accessToken, seedTracks, seedArtists, seedGenres, limit, market));
    }

    @GetMapping("/recommendations/genres")
    public SpotifyEnvelope recommendationGenres(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken) {
        return new SpotifyEnvelope(spotifyService.getRecommendationGenres(accessToken));
    }

    // ── Artist ────────────────────────────────────────────────────────────────

    @GetMapping("/artists/{artistId}")
    public SpotifyEnvelope artist(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String artistId) {
        return new SpotifyEnvelope(spotifyService.getArtist(accessToken, artistId));
    }

    @GetMapping("/artists/{artistId}/albums")
    public SpotifyEnvelope artistAlbums(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String artistId,
            @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(spotifyService.getArtistAlbums(accessToken, artistId, limit));
    }

    @GetMapping("/artists/{artistId}/top-tracks")
    public SpotifyEnvelope artistTopTracks(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String artistId,
            @RequestParam(required = false) String market) {
        return new SpotifyEnvelope(spotifyService.getArtistTopTracks(accessToken, artistId, market));
    }

    @GetMapping("/artists/{artistId}/related")
    public SpotifyEnvelope relatedArtists(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String artistId) {
        return new SpotifyEnvelope(spotifyService.getRelatedArtists(accessToken, artistId));
    }

    // ── Album ─────────────────────────────────────────────────────────────────

    @GetMapping("/albums/{albumId}")
    public SpotifyEnvelope album(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String albumId) {
        return new SpotifyEnvelope(spotifyService.getAlbum(accessToken, albumId));
    }

    @GetMapping("/albums/{albumId}/tracks")
    public SpotifyEnvelope albumTracks(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String albumId,
            @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(spotifyService.getAlbumTracks(accessToken, albumId, limit));
    }

    // ── Playlist ──────────────────────────────────────────────────────────────

    @GetMapping("/playlists/{playlistId}")
    public SpotifyEnvelope playlist(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String playlistId) {
        return new SpotifyEnvelope(spotifyService.getPlaylist(accessToken, playlistId));
    }

    @GetMapping("/playlists/{playlistId}/tracks")
    public SpotifyEnvelope playlistTracks(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String playlistId,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {
        return new SpotifyEnvelope(spotifyService.getPlaylistTracks(accessToken, playlistId, limit, offset));
    }

    // ── Track ─────────────────────────────────────────────────────────────────

    @GetMapping("/tracks/{trackId}")
    public SpotifyEnvelope track(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String trackId) {
        return new SpotifyEnvelope(spotifyService.getTrack(accessToken, trackId));
    }

    @GetMapping("/tracks/{trackId}/features")
    public SpotifyEnvelope audioFeatures(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String trackId) {
        return new SpotifyEnvelope(spotifyService.getTrackAudioFeatures(accessToken, trackId));
    }
}