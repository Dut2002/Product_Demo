import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiHeaders, ApiUrls } from '../../../constant/api.const.urls';
import { AuthService } from '../../../service/auth/auth.service';
import { RouterUrl } from '../../../constant/app.const.router';

@Component({
  selector: 'app-forbidden',
  templateUrl: './forbidden.component.html',
  styleUrl: './forbidden.component.scss'
})
export class ForbiddenComponent {
  constructor( private router: Router, private authService: AuthService) {
  }

  goBack(): void {
    const previousUrl = sessionStorage.getItem(ApiHeaders.PREVIOUS);
    if(previousUrl) this.router.navigate([previousUrl]);
    else this.goToLogin();
  }

  // Điều hướng về trang login
  goToLogin(): void {
    this.authService.clearLocalStorage();
    this.router.navigate([RouterUrl.LOG_IN]);
  }
}
