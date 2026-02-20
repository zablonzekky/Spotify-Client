import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map, Observable } from 'rxjs';
import { SearchResponse, SpotifyItem } from '../models/spotify.models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/spotify';

  getFeatured(): Observable<SpotifyItem[]> {
    return this.http.get<{ payload: { playlists: { items: SpotifyItem[] } } }>(`${this.baseUrl}/featured`)
      .pipe(map((res) => res.payload.playlists.items ?? []));
  }

  search(query: string): Observable<SearchResponse> {
    return this.http.post<{ payload: SearchResponse }>(`${this.baseUrl}/search`, { query })
      .pipe(map((res) => res.payload));
  }
}
