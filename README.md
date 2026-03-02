# Spotify Client (Angular + Spring Boot)

Production-oriented starter for a Spotify client with:

- Angular 19 standalone frontend
- Spring Boot 3 backend
- OAuth Authorization Code flow handling in backend
- Typed API integration service and centralized error handling
- Debounced search UX and clean component architecture

## Repository structure

- `frontend/` Angular UI
- `backend/` Spring Boot API

## Backend setup

Set environment variables:

```bash
export SPOTIFY_CLIENT_ID=your_client_id
export SPOTIFY_CLIENT_SECRET=your_client_secret
export SPOTIFY_REDIRECT_URI=http://localhost:8080/api/auth/callback
export FRONTEND_BASE_URL=http://localhost:4200
```

Run:

```bash
cd backend
./mvnw spring-boot:run
```

If `mvnw` is not present, use local maven:

```bash
mvn spring-boot:run
```

## Frontend setup

```bash
cd frontend
npm install
npm start
```

## API endpoints

- `GET /api/auth/login` -> Spotify authorization URL
- `GET /api/auth/callback` -> OAuth callback and session cookie
- `GET /api/auth/session` -> auth presence
- `GET /api/spotify/me` -> user profile
- `GET /api/spotify/search?q=` -> multi-type search

## Next production steps

- Replace in-memory token store with Redis or encrypted DB store
- Add CSRF + state/nonce persistence and validation
- Add refresh-token rotation safeguards
- Add structured logging, tracing, and dashboards
- Add e2e tests for OAuth callback and search flow
