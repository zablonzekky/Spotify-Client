package com.spotifyclient.controller;

import com.spotifyclient.dto.AuthTokenResponse;
import com.spotifyclient.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/authorize")
    public Map<String, String> authorizeUrl(@RequestParam @NotBlank String state,
                                            @RequestParam("challenge") @NotBlank String codeChallenge) {
        return Map.of("authorizeUrl", authService.buildAuthorizeUrl(state, codeChallenge));
    }

    @PostMapping("/token")
    public AuthTokenResponse exchangeToken(@RequestParam @NotBlank String code,
                                           @RequestParam("verifier") @NotBlank String codeVerifier) {
        return authService.exchangeCode(code, codeVerifier);
    }
}
