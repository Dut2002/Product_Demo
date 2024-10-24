import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth/auth.service';
import { inject } from '@angular/core';
import { RouterUrl } from '../constant/app.const.router';
import { ApiHeaders } from '../constant/api.const.urls';

export const authGuard: CanActivateFn = async (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  console.log(route);
  console.log(state);


  if (authService.hasPermission(route.routeConfig?.path!)) {
    sessionStorage.setItem(ApiHeaders.PREVIOUS, state.url); // Lưu URL có quyền
    return true; // Cho phép truy cập nếu có JWT
  } else {
    router.navigate([RouterUrl.FORBIDDEN]);
    return false;
  }
};

