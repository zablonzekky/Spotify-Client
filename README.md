# Spotify Client (Angular + Spring Boot)

Production-grade starter for a Spotify client with:

- **Angular 17** frontend for responsive UX and player-focused navigation
- **Spring Boot 3** backend for OAuth handling, secure token exchange, API aggregation, retry/caching, and observability

## Monorepo layout

- `frontend/` Angular application
- `backend/` Spring Boot API gateway to Spotify Web API
- `docs/` architecture and delivery plan

## Backend quickstart

```bash
cd backend
mvn spring-boot:run
```

Environment variables:

- `SPOTIFY_CLIENT_ID`
- `SPOTIFY_CLIENT_SECRET`
- `SPOTIFY_REDIRECT_URI`

## Frontend quickstart

```bash
cd frontend
npm install
npm start
```

## Core capabilities scaffolded

- OAuth authorize URL and token exchange endpoints
- Featured playlists and new releases browsing
- Debounced search
- Caffeine caching + retry for rate-limited requests
- Player-oriented UX shell with persistent mini-player
- Strict TypeScript + strict Angular template checks
