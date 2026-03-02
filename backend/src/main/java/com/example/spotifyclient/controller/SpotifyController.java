package com.example.spotifyclient.controller;

import com.example.spotifyclient.exception.UnauthorizedException;
import com.example.spotifyclient.service.SpotifyService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/spotify")
@Validated
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/me")
    public Map<String, Object> me(jakarta.servlet.http.HttpServletRequest request) {
        String sessionId = AuthController.readSessionId(request);
        if (sessionId == null) {
            throw new UnauthorizedException("Missing session. Authenticate first.");
        }
        return spotifyService.me(sessionId);
    }

    @GetMapping("/search")
    public Map<String, Object> search(
            jakarta.servlet.http.HttpServletRequest request,
            @RequestParam @NotBlank String q
    ) {
        String sessionId = AuthController.readSessionId(request);
        if (sessionId == null) {
            throw new UnauthorizedException("Missing session. Authenticate first.");
        }
        return spotifyService.search(sessionId, q);
    }
}
