# Architecture

## Design standards

1. **Clean separation of concerns**
   - Backend controllers map HTTP contracts only.
   - Service layer encapsulates business use cases.
   - Spotify API client centralizes retries and API semantics.
2. **Reliability first**
   - Retry with exponential backoff for 429-like failures.
   - Caching for browse endpoints to reduce latency and quota consumption.
3. **Security-aware defaults**
   - Backend handles token exchange using client secret.
   - Frontend never stores client secret.
4. **UX consistency**
   - Persistent nav + mini-player.
   - Search optimized with debouncing.
   - Responsive card-based layout.

## Suggested next steps

- Add refresh token persistence in encrypted server-side store.
- Add device playback endpoints and queue controls.
- Integrate OpenAPI spec generation and contract testing.
- Implement NgRx store for state-heavy playback workflows.
