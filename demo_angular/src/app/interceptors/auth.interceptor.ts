import { HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { AuthService } from '../service/auth/auth.service';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { RouterUrl } from '../constant/app.const.router';

export function authenInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const authService = inject(AuthService);
  const token = authService.getToken();
  const router = inject(Router)

  let request = req;

  if (token) {
    if (authService.isTokenExpired(token)) {
      authService.clearLocalStorage();
      router.navigate([RouterUrl.LOG_IN]);
    } else {
      request = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
  }
  return next(request);
}
