import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth/auth.service';
import { inject } from '@angular/core';
import { RouterUrl } from '../constant/app.const.router';
import { ApiHeaders } from '../constant/api.const.urls';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const authority = route.data[ApiHeaders.AUTHORITY];
  if(authService.isTokenExpired(authService.getToken())){

    if(!authService.sendRefreshToken()){
      router.navigate([RouterUrl.LOG_IN.path]);
      return false;
    }
  }
  if (authService.hasAuthority(authority)) {
    sessionStorage.setItem(ApiHeaders.PREVIOUS, state.url); // Lưu URL có quyền
    return true; // Cho phép truy cập nếu có JWT
  } else {
    router.navigate([RouterUrl.FORBIDDEN.path]);
    return false;
  }
};

