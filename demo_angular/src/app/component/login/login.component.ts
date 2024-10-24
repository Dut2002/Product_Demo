import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from '../../service/login/login.service';
import { ApiHeaders, ApiStatus } from '../../constant/api.const.urls';
import { SnackBarService } from '../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../service/error-handle/error-handle.service';
import { Router } from '@angular/router';
import { RouterUrl } from '../../constant/app.const.router';
import { AuthService } from '../../service/auth/auth.service';
import { LoginRequest } from '../../model/dto/login-request';
import { finalize } from 'rxjs';

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
    private snackBarService: SnackBarService,
    private errorHandleService: ErrorHandleService,
    private router: Router,
    private authService: AuthService
  ) {
    if (!authService.isTokenExpired(authService.getRefresh())) {
      router.navigate([RouterUrl.HOME])
    }
  }

  login(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.loginService.login(this.loginRequest).pipe(finalize(()=>{
        this.isLoading = false;
      }))
      .subscribe({
        next: (response) => {
          localStorage.setItem(ApiHeaders.TOKEN_KEY, response.token);
          localStorage.setItem(ApiHeaders.REFRESH_KEY, response.refreshToken);
          localStorage.setItem(ApiHeaders.ROLE_KEY, response.roles[0])
          this.snackBarService.show(null, "Login Success!", ApiStatus.SUCCESS, 5000)
          this.router.navigate([RouterUrl.HOME])
        },
        error: (error) => {
          this.errorHandleService.handle(error);
        },
        complete: () => {
          this.isLoading = false;
          console.log('Log in request completed');
        }
      })
    } else {
      this.loginForm.control.markAllAsTouched();
    }
  }



}
