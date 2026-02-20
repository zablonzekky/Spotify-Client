export interface SpotifyItem {
  id: string;
  name: string;
  description?: string;
  imageUrl?: string;
}

export interface SearchResponse {
  tracks?: { items: SpotifyItem[] };
  artists?: { items: SpotifyItem[] };
  albums?: { items: SpotifyItem[] };
  playlists?: { items: SpotifyItem[] };
}
