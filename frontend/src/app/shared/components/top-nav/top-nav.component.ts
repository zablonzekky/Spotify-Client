import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-top-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  styles: [`
    nav { display:flex; justify-content:space-between; align-items:center; padding:1rem 1.5rem; border-bottom:1px solid rgba(255,255,255,0.08); background:#11141a; }
    .links { display:flex; gap:1rem; }
    a { color:var(--muted); text-decoration:none; }
    a.active { color:var(--text); }
  `],
  template: `
    <nav>
      <strong>Spotify Client Pro</strong>
      <div class="links">
        <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">Home</a>
        <a routerLink="/search" routerLinkActive="active">Search</a>
        <a routerLink="/library" routerLinkActive="active">Library</a>
      </div>
    </nav>
  `
})
export class TopNavComponent {}
