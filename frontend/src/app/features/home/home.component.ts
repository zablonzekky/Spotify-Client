import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter, switchMap } from 'rxjs';
import { SpotifyApiService } from '../../core/services/spotify-api.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  private readonly api = inject(SpotifyApiService);

  readonly profile = signal<string>('Not connected');
  readonly tracks = signal<Array<{ id: string; name: string; artists: string }>>([]);
  readonly searchControl = new FormControl('', { nonNullable: true });

  constructor() {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter((value) => value.length > 1),
        switchMap((value) => this.api.search(value))
      )
      .subscribe((response) => {
        const mapped = (response.tracks?.items ?? []).map((track) => ({
          id: track.id,
          name: track.name,
          artists: track.artists.map((artist) => artist.name).join(', ')
        }));
        this.tracks.set(mapped);
      });
  }

  connect(): void {
    this.api.loginUrl().subscribe(({ authorizationUrl }) => {
      window.location.href = authorizationUrl;
    });
  }

  loadProfile(): void {
    this.api.me().subscribe((profile) => {
      this.profile.set(`${profile.display_name} (${profile.product})`);
    });
  }
}
