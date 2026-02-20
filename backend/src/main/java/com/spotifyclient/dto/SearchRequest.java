package com.spotifyclient.dto;

import jakarta.validation.constraints.NotBlank;

public record SearchRequest(
        @NotBlank String query,
        String types,
        Integer limit,
        String market
) {
}
