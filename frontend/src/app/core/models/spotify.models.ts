export interface AuthUrlResponse {
  authorizationUrl: string;
}

export interface SessionResponse {
  authenticated: boolean;
}

export interface UserProfile {
  display_name: string;
  email: string;
  country: string;
  product: string;
}

export interface SearchResponse {
  tracks?: { items: Array<{ id: string; name: string; artists: Array<{ name: string }> }> };
}
