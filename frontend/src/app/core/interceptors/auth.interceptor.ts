import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken();
  const securedReq = token
    ? req.clone({ setHeaders: { 'X-Spotify-Token': token } })
    : req;

  return next(securedReq);
};
