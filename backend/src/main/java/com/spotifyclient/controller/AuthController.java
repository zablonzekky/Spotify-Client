package com.spotifyclient.controller;

import com.spotifyclient.dto.AuthTokenResponse;
import com.spotifyclient.service.AuthService;
import com.spotifyclient.spotify.SpotifyApiClient;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;
    private final SpotifyApiClient spotifyApiClient;

    public AuthController(AuthService authService, SpotifyApiClient spotifyApiClient) {
        this.authService = authService;
        this.spotifyApiClient = spotifyApiClient;
    }

    /**
     * Step 1 of PKCE flow: build the Spotify authorize URL.
     * Frontend redirects the user to the returned URL.
     */
    @GetMapping("/authorize")
    public Map<String, String> authorizeUrl(
            @RequestParam @NotBlank String state,
            @RequestParam("challenge") @NotBlank String codeChallenge) {
        return Map.of("authorizeUrl", authService.buildAuthorizeUrl(state, codeChallenge));
    }

    /**
     * Step 2 of PKCE flow: exchange the authorization code for tokens.
     * Called from the Angular /callback route after Spotify redirects back.
     */
    @PostMapping("/token")
    public AuthTokenResponse exchangeToken(
            @RequestParam @NotBlank String code,
            @RequestParam("verifier") @NotBlank String codeVerifier) {
        return authService.exchangeCode(code, codeVerifier);
    }

    /**
     * Refresh an expired access token using the refresh token.
     * Frontend should call this automatically before the token expires.
     */
    @PostMapping("/refresh")
    public AuthTokenResponse refreshToken(
            @RequestParam("refresh_token") @NotBlank String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    /**
     * Fetch the currently authenticated Spotify user's profile.
     */
    @GetMapping("/me")
    public Map<String, Object> me(@RequestHeader("X-Spotify-Token") @NotBlank String accessToken) {
        return spotifyApiClient.get("/me", accessToken, Map.of());
    }
}