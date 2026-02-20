import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TopNavComponent } from './shared/components/top-nav/top-nav.component';
import { MiniPlayerComponent } from './shared/components/mini-player/mini-player.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TopNavComponent, MiniPlayerComponent],
  template: `
    <app-top-nav />
    <main style="padding: 1.5rem; padding-bottom: 6rem; max-width: 1200px; margin: 0 auto;">
      <router-outlet />
    </main>
    <app-mini-player />
  `
})
export class AppComponent {}
