import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../../service/login/login.service';
import { ApiHeaders, ApiStatus, ApiUrls } from '../../constant/api.const.urls';
import { SnackBarService } from '../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../service/error-handle/error-handle.service';
import { Router } from '@angular/router';
import { RouterUrl } from '../../constant/app.const.router';
import { Role } from '../../constant/app.const.role';
import { AuthService } from '../../service/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;


  constructor(private loginService: LoginService,
    private fb: FormBuilder,
    private snackBarService: SnackBarService,
    private errorHandleService: ErrorHandleService,
    private router: Router,
    private authService: AuthService
  ) {
    alert('refresh: ' + authService.getRefresh())
    if (!authService.isTokenExpired(authService.getRefresh())) {
      router.navigate([RouterUrl.BASE_URL.path])
    }
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required]],
    });
  }

  login(): void {
    if (this.loginForm.valid) {
      this.loginService.login(this.loginForm.value).subscribe({
        next: (response) => {
          localStorage.setItem(ApiHeaders.TOKEN_KEY, response.token);
          localStorage.setItem(ApiHeaders.REFRESH_KEY, response.refreshToken);
          localStorage.setItem(ApiHeaders.ROLE_KEY, response.roles[0])
          this.snackBarService.show(null, "Login Success!", ApiStatus.SUCCESS, 5000)
          this.router.navigate([RouterUrl.BASE_URL.path])
        },
        error: (error) => {
          this.errorHandleService.handle(error);
        },
        complete: () => {
          console.log('Log in request completed');
        }
      })
    } else {
      this.snackBarService.show('Form is invalid', 'Please check your input data.', ApiStatus.ERROR, 5000)
    }
  }



}
