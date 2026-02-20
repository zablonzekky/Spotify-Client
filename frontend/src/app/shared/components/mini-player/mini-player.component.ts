import { Component } from '@angular/core';

@Component({
  selector: 'app-mini-player',
  standalone: true,
  styles: [`
    footer { position:fixed; bottom:0; left:0; right:0; background:#0b0d11; border-top:1px solid rgba(255,255,255,0.08); padding:1rem 1.5rem; display:flex; justify-content:space-between; align-items:center; }
    button { background:var(--accent); color:#03200d; border:0; padding:0.5rem 1rem; border-radius:999px; font-weight:700; }
  `],
  template: `
    <footer>
      <div>
        <strong>Not Playing</strong>
        <div style="color:var(--muted)">Select a track to start listening</div>
      </div>
      <button aria-label="Play or pause">Play</button>
    </footer>
  `
})
export class MiniPlayerComponent {}
