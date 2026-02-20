package com.spotifyclient.controller;

import com.spotifyclient.dto.SearchRequest;
import com.spotifyclient.dto.SpotifyEnvelope;
import com.spotifyclient.service.SpotifyService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/featured")
    public SpotifyEnvelope featured(@RequestHeader("X-Spotify-Token") String accessToken,
                                    @RequestParam(defaultValue = "en_US") String locale,
                                    @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(spotifyService.getFeaturedPlaylists(accessToken, locale, limit));
    }

    @GetMapping("/new-releases")
    public SpotifyEnvelope newReleases(@RequestHeader("X-Spotify-Token") String accessToken,
                                       @RequestParam(required = false) Integer limit) {
        return new SpotifyEnvelope(spotifyService.getNewReleases(accessToken, limit));
    }

    @PostMapping("/search")
    public SpotifyEnvelope search(@RequestHeader("X-Spotify-Token") String accessToken,
                                  @Valid @RequestBody SearchRequest request) {
        return new SpotifyEnvelope(spotifyService.search(accessToken, request));
    }
}
