import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { PlaylistCardComponent } from '../../shared/components/playlist-card/playlist-card.component';

@Component({
  standalone: true,
  imports: [AsyncPipe, PlaylistCardComponent],
  template: `
    <section>
      <h1>Featured Playlists</h1>
      <p style="color:var(--muted)">Curated mixes and daily listening recommendations.</p>
      @if (featured$ | async; as featured) {
        <div class="page-grid">
          @for (item of featured; track item.id) {
            <app-playlist-card [item]="item" />
          }
        </div>
      }
    </section>
  `
})
export class HomeComponent {
  protected readonly featured$ = inject(ApiService).getFeatured();
}
