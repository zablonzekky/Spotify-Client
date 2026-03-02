package com.example.spotifyclient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spotify.client-id=test",
        "spotify.client-secret=test",
        "spotify.redirect-uri=http://localhost/callback"
})
class SpotifyClientApplicationTests {

    @Test
    void contextLoads() {
    }
}
