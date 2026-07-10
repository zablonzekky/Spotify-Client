package com.spotifyclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableRetry
@EnableScheduling
public class SpotifyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyClientApplication.class, args);
    }
}