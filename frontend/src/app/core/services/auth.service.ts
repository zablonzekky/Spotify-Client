import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {
  getToken(): string {
    return localStorage.getItem('spotify_access_token') ?? '';
  }
}
