import { Component, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { AsyncPipe } from '@angular/common';
import { debounceTime, distinctUntilChanged, startWith, switchMap } from 'rxjs';
import { ApiService } from '../../core/services/api.service';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, AsyncPipe],
  template: `
    <section>
      <h1>Search</h1>
      <input [formControl]="query" type="search" placeholder="Search tracks, artists, playlists" style="width:100%; padding:0.75rem; border-radius:10px; border:1px solid rgba(255,255,255,0.2); background:#11141a; color:var(--text);">

      @if (results$ | async; as result) {
        <div class="card" style="margin-top:1rem">
          <h3>Top Results</h3>
          <p style="color:var(--muted)">Tracks: {{ result.tracks?.items?.length || 0 }} | Artists: {{ result.artists?.items?.length || 0 }}</p>
        </div>
      }
    </section>
  `
})
export class SearchComponent {
  private readonly api = inject(ApiService);
  protected readonly query = new FormControl('', { nonNullable: true });
  protected readonly results$ = this.query.valueChanges.pipe(
    startWith(''),
    debounceTime(350),
    distinctUntilChanged(),
    switchMap((query) => this.api.search(query))
  );
}
