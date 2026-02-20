import { Component } from '@angular/core';

@Component({
  standalone: true,
  template: `
    <section>
      <h1>Your Library</h1>
      <div class="card">
        <p>Library operations are scaffolded for Spotify saved tracks/albums and playlist management.</p>
        <ul>
          <li>Save/Unsave tracks</li>
          <li>Create playlists</li>
          <li>Follow artists</li>
        </ul>
      </div>
    </section>
  `
})
export class LibraryComponent {}
