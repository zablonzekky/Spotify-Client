import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  standalone: true,
  template: `
    <header>
      <h1>Spotify Client</h1>
      <p>Angular + Spring Boot production starter</p>
    </header>
  `,
  styles: [
    `
      header {
        border-bottom: 1px solid #2f3542;
        padding: 1rem 1.5rem;
        background: #151923;
      }
      h1 {
        margin: 0;
        font-size: 1.25rem;
      }
      p {
        margin: 0.3rem 0 0;
        color: #a5afc1;
        font-size: 0.9rem;
      }
    `
  ]
})
export class NavbarComponent {}
