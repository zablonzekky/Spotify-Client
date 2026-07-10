package com.spotifyclient.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TrackUrisRequest(
        @NotEmpty List<String> uris
) {}