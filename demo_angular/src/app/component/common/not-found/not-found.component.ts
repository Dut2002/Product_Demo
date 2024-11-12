import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiHeaders, ApiUrls } from '../../../constant/api.const.urls';
import { RouterUrl } from '../../../constant/app.const.router';
import { AuthService } from '../../../service/auth/auth.service';

@Component({
  selector: 'app-not-found',
  templateUrl: './not-found.component.html',
  styleUrl: './not-found.component.scss'
})
export class NotFoundComponent {
  constructor( private router: Router, private authen : AuthService) {
  }

  goBack(): void {
    const previousUrl = sessionStorage.getItem(ApiHeaders.PREVIOUS);
    if(previousUrl) this.router.navigate([previousUrl]);
    else this.goToLogin();
  }

  // Điều hướng về trang login
  goToLogin(): void {
    this.authen.clearLocalStorage();
    this.router.navigate([RouterUrl.LOG_IN]);
  }
}
