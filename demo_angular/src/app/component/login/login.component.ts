import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from '../../service/login/login.service';
import { ApiHeaders, ApiStatus } from '../../constant/api.const.urls';
import { SnackBarService } from '../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../service/error-handle/error-handle.service';
import { Router } from '@angular/router';
import { RouterUrl } from '../../constant/app.const.router';
import { LoginRequest } from '../../model/dto/login-request';
import { finalize } from 'rxjs';
import { Role } from '../../constant/app.const.role';
import { CommonService } from '../../service/common/common.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  loginRequest: LoginRequest = {} as LoginRequest
  isLoading: boolean = false;

  @ViewChild('loginForm', { static: false }) loginForm!: NgForm;


  constructor(private loginService: LoginService,
    private common: CommonService,
    private router: Router,
  ) {
    this.redirectToPage();
  }

  login(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;

      this.loginService.login(this.loginRequest)
        .pipe(finalize(() => {
          this.isLoading = false;
        }))
        .subscribe({
          next: (response) => {
            localStorage.setItem(ApiHeaders.TOKEN_KEY, response.token);
            localStorage.setItem(ApiHeaders.REFRESH_KEY, response.refreshToken);
            localStorage.setItem(ApiHeaders.ROLE_KEY, response.roles)

            this.common.snackBar.show(null, "Login Success!", ApiStatus.SUCCESS, 5000)
            this.common.auth.startRefreshTimer();
            this.redirectToPage();
          },
          error: (error) => {
            this.common.errorHandle.handle(error);
          }
        })
    } else {
      this.loginForm.control.markAllAsTouched();
    }
  }

  redirectToPage() {
    const roles = this.common.auth.getRoles();
    if (!this.common.auth.isTokenExpired() && roles.length > 0) {
      if (roles.includes(Role.ADMIN)) {
        this.router.navigate([RouterUrl.USER_ROLE]);
      } else if (roles.includes(Role.STAFF)) {
        this.router.navigate([RouterUrl.PRODUCT_MANAGEMENT]);
      } else if (roles.includes(Role.CUSTOMER)) {
        this.router.navigate([RouterUrl.SHOPPING]);
      } else {
        this.common.auth.clearLocalStorage();
      }
    }
  }
}
