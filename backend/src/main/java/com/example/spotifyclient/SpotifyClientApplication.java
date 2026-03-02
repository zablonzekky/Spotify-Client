package com.example.spotifyclient;

import com.example.spotifyclient.config.SpotifyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SpotifyProperties.class)
public class SpotifyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyClientApplication.class, args);
    }
}
