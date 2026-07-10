package com.spotifyclient.controller;

import com.spotifyclient.dto.PlayRequest;
import com.spotifyclient.dto.SpotifyEnvelope;
import com.spotifyclient.service.PlayerService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/player")
@Validated
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/state")
    public SpotifyEnvelope getPlaybackState(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken) {
        return new SpotifyEnvelope(playerService.getPlaybackState(accessToken));
    }

    @GetMapping("/current")
    public SpotifyEnvelope getCurrentlyPlaying(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken) {
        return new SpotifyEnvelope(playerService.getCurrentlyPlaying(accessToken));
    }

    @GetMapping("/devices")
    public SpotifyEnvelope getDevices(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken) {
        return new SpotifyEnvelope(playerService.getAvailableDevices(accessToken));
    }

    @PutMapping("/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferPlayback(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam String deviceId,
            @RequestParam(defaultValue = "false") boolean play) {
        playerService.transferPlayback(accessToken, deviceId, play);
    }

    @PutMapping("/play")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void play(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestBody(required = false) PlayRequest request,
            @RequestParam(required = false) String deviceId) {
        if (request == null) {
            playerService.play(accessToken, deviceId, null, null, null, null);
        } else {
            playerService.play(accessToken, deviceId,
                    request.contextUri(), request.uris(),
                    request.offsetPosition(), request.positionMs());
        }
    }

    @PutMapping("/pause")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pause(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) String deviceId) {
        playerService.pause(accessToken, deviceId);
    }

    @PostMapping("/next")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void skipToNext(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) String deviceId) {
        playerService.skipToNext(accessToken, deviceId);
    }

    @PostMapping("/previous")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void skipToPrevious(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam(required = false) String deviceId) {
        playerService.skipToPrevious(accessToken, deviceId);
    }

    @PutMapping("/seek")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void seek(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam int positionMs,
            @RequestParam(required = false) String deviceId) {
        playerService.seek(accessToken, positionMs, deviceId);
    }

    @PutMapping("/volume")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVolume(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam @Min(0) @Max(100) int volumePercent,
            @RequestParam(required = false) String deviceId) {
        playerService.setVolume(accessToken, volumePercent, deviceId);
    }

    @PutMapping("/shuffle")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setShuffle(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam boolean state,
            @RequestParam(required = false) String deviceId) {
        playerService.setShuffle(accessToken, state, deviceId);
    }

    @PutMapping("/repeat")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setRepeat(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam String state,
            @RequestParam(required = false) String deviceId) {
        playerService.setRepeat(accessToken, state, deviceId);
    }

    @PostMapping("/queue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToQueue(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken,
            @RequestParam String uri,
            @RequestParam(required = false) String deviceId) {
        playerService.addToQueue(accessToken, uri, deviceId);
    }

    @GetMapping("/queue")
    public SpotifyEnvelope getQueue(
            @RequestHeader("X-Spotify-Token") @NotBlank String accessToken) {
        return new SpotifyEnvelope(playerService.getQueue(accessToken));
    }
}
