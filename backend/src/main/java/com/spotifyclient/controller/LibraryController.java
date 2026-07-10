package com.spotifyclient.controller;

import com.spotifyclient.dto.CreatePlaylistRequest;
import com.spotifyclient.dto.SpotifyEnvelope;
import com.spotifyclient.dto.TrackUrisRequest;
import com.spotifyclient.service.LibraryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library")
@Validated
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // ── Saved Tracks ─────────────────────────────────────────────────────────

    @GetMapping("/tracks")
    public SpotifyEnvelope getSavedTracks(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {
        return new SpotifyEnvelope(libraryService.getSavedTracks(accessToken, limit, offset));
    }

    @PutMapping("/tracks/{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveTrack(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String trackId) {
        libraryService.saveTrack(accessToken, trackId);
    }

    @DeleteMapping("/tracks/{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTrack(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String trackId) {
        libraryService.removeTrack(accessToken, trackId);
    }

    @GetMapping("/tracks/contains")
    public SpotifyEnvelope checkSavedTracks(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam List<String> ids) {
        return new SpotifyEnvelope(libraryService.checkSavedTracks(accessToken, ids));
    }

    // ── Saved Albums ─────────────────────────────────────────────────────────

    @GetMapping("/albums")
    public SpotifyEnvelope getSavedAlbums(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {
        return new SpotifyEnvelope(libraryService.getSavedAlbums(accessToken, limit, offset));
    }

    @PutMapping("/albums/{albumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveAlbum(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String albumId) {
        libraryService.saveAlbum(accessToken, albumId);
    }

    @DeleteMapping("/albums/{albumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAlbum(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String albumId) {
        libraryService.removeAlbum(accessToken, albumId);
    }

    // ── User Playlists ────────────────────────────────────────────────────────

    @GetMapping("/playlists")
    public SpotifyEnvelope getUserPlaylists(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {
        return new SpotifyEnvelope(libraryService.getUserPlaylists(accessToken, limit, offset));
    }

    @PostMapping("/playlists")
    @ResponseStatus(HttpStatus.CREATED)
    public SpotifyEnvelope createPlaylist(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam String userId,
            @Valid @RequestBody CreatePlaylistRequest request) {
        return new SpotifyEnvelope(libraryService.createPlaylist(
                accessToken, userId, request.name(),
                request.description(), request.isPublic()));
    }

    @PostMapping("/playlists/{playlistId}/tracks")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTracksToPlaylist(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String playlistId,
            @Valid @RequestBody TrackUrisRequest request) {
        libraryService.addTracksToPlaylist(accessToken, playlistId, request.uris());
    }

    @DeleteMapping("/playlists/{playlistId}/tracks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTracksFromPlaylist(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String playlistId,
            @Valid @RequestBody TrackUrisRequest request) {
        libraryService.removeTracksFromPlaylist(accessToken, playlistId, request.uris());
    }

    // ── Followed Artists ─────────────────────────────────────────────────────

    @GetMapping("/following")
    public SpotifyEnvelope getFollowedArtists(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(libraryService.getFollowedArtists(accessToken, limit));
    }

    @PutMapping("/following/{artistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void followArtist(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String artistId) {
        libraryService.followArtist(accessToken, artistId);
    }

    @DeleteMapping("/following/{artistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollowArtist(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String artistId) {
        libraryService.unfollowArtist(accessToken, artistId);
    }

    // ── History & Top Items ───────────────────────────────────────────────────

    @GetMapping("/history")
    public SpotifyEnvelope getRecentlyPlayed(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(libraryService.getRecentlyPlayed(accessToken, limit));
    }

    @GetMapping("/top/{type}")
    public SpotifyEnvelope getTopItems(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @PathVariable String type,
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(libraryService.getTopItems(accessToken, type, timeRange, limit));
    }
}
