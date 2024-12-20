import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth/auth.service';
import { inject } from '@angular/core';
import { RouterUrl } from '../constant/app.const.router';
import { ApiHeaders } from '../constant/api.const.urls';

export const authGuard: CanActivateFn = async (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if(authService.isTokenExpired()){
    authService.clearLocalStorage();
    router.navigate([RouterUrl.LOG_IN]);
    return false;
  }
  const functionName = authService.getFunction(route.routeConfig?.path!);
  if (functionName) {
    sessionStorage.setItem(ApiHeaders.PREVIOUS, state.url); // Lưu URL có quyền
    authService.startRefreshTimer();
    return true; // Cho phép truy cập nếu có JWT
  } else {
    router.navigate([RouterUrl.FORBIDDEN]);
    return false;
  }
};

