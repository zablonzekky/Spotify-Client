import { Component, Input } from '@angular/core';
import { SpotifyItem } from '../../../core/models/spotify.models';

@Component({
  selector: 'app-playlist-card',
  standalone: true,
  template: `
    <article class="card">
      <h3 style="margin-top:0">{{ item.name }}</h3>
      <p style="margin-bottom:0; color:var(--muted)">{{ item.description || 'Curated playlist' }}</p>
    </article>
  `
})
export class PlaylistCardComponent {
  @Input({ required: true }) item!: SpotifyItem;
}
