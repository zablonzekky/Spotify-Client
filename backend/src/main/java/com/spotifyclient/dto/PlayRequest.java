package com.spotifyclient.dto;

import java.util.List;

public record PlayRequest(
        String contextUri,
        List<String> uris,
        Integer offsetPosition,
        Integer positionMs
) {}