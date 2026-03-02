package com.example.spotifyclient.controller;

import com.example.spotifyclient.config.SpotifyProperties;
import com.example.spotifyclient.dto.AuthUrlResponse;
import com.example.spotifyclient.dto.SessionResponse;
import com.example.spotifyclient.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String SESSION_COOKIE = "sp_session";

    private final AuthService authService;
    private final SpotifyProperties properties;

    public AuthController(AuthService authService, SpotifyProperties properties) {
        this.authService = authService;
        this.properties = properties;
    }

    @GetMapping("/login")
    public AuthUrlResponse login() {
        String state = UUID.randomUUID().toString();
        return new AuthUrlResponse(authService.createAuthorizationUrl(state));
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state,
            jakarta.servlet.http.HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String currentSessionId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (SESSION_COOKIE.equals(cookie.getName())) {
                    currentSessionId = cookie.getValue();
                }
            }
        }

        String sessionId = authService.exchangeCode(code, currentSessionId);

        Cookie sessionCookie = new Cookie(SESSION_COOKIE, sessionId);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        sessionCookie.setSecure(false);
        sessionCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(sessionCookie);

        response.sendRedirect(properties.frontendBaseUrl());
    }

    @GetMapping("/session")
    public ResponseEntity<SessionResponse> session(jakarta.servlet.http.HttpServletRequest request) {
        String sessionId = readSessionId(request);
        boolean authenticated = sessionId != null;
        return ResponseEntity.ok(new SessionResponse(authenticated));
    }

    static String readSessionId(jakarta.servlet.http.HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (SESSION_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
