import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthUrlResponse, SearchResponse, SessionResponse, UserProfile } from '../models/spotify.models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SpotifyApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  loginUrl(): Observable<AuthUrlResponse> {
    return this.http.get<AuthUrlResponse>(`${this.baseUrl}/auth/login`, { withCredentials: true });
  }

  session(): Observable<SessionResponse> {
    return this.http.get<SessionResponse>(`${this.baseUrl}/auth/session`, { withCredentials: true });
  }

  me(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.baseUrl}/spotify/me`, { withCredentials: true });
  }

  search(query: string): Observable<SearchResponse> {
    const params = new HttpParams().set('q', query);
    return this.http.get<SearchResponse>(`${this.baseUrl}/spotify/search`, { params, withCredentials: true });
  }
}
