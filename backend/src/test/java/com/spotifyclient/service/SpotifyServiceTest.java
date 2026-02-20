package com.spotifyclient.service;

import com.spotifyclient.dto.SearchRequest;
import com.spotifyclient.spotify.SpotifyApiClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SpotifyServiceTest {

    private final SpotifyApiClient spotifyApiClient = Mockito.mock(SpotifyApiClient.class);
    private final SpotifyService spotifyService = new SpotifyService(spotifyApiClient);

    @Test
    void searchUsesDefaultsWhenMissingOptionalFields() {
        Mockito.when(spotifyApiClient.get(Mockito.eq("/search"), Mockito.eq("token"), Mockito.anyMap()))
                .thenReturn(Map.of("ok", true));

        Map<String, Object> response = spotifyService.search("token", new SearchRequest("Muse", null, null, null));

        assertThat(response).containsEntry("ok", true);
        Mockito.verify(spotifyApiClient).get("/search", "token", Map.of(
                "q", "Muse",
                "type", "track,artist,album,playlist",
                "limit", 10,
                "market", "US"
        ));
    }
}
