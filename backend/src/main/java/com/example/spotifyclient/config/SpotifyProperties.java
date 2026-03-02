package com.example.spotifyclient.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spotify")
public record SpotifyProperties(
        @NotBlank String clientId,
        @NotBlank String clientSecret,
        @NotBlank String redirectUri,
        String scopes,
        String accountsBaseUrl,
        String apiBaseUrl,
        String frontendBaseUrl
) {
}
