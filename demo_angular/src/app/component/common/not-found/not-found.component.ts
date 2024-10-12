import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiHeaders, ApiUrls } from '../../../constant/api.const.urls';

@Component({
  selector: 'app-not-found',
  templateUrl: './not-found.component.html',
  styleUrl: './not-found.component.scss'
})
export class NotFoundComponent {
  constructor( private router: Router) {
  }

  goBack(): void {
    const previousUrl = sessionStorage.getItem(ApiHeaders.PREVIOUS);
    if(previousUrl) this.router.navigate([previousUrl]);
    else this.goToLogin();
  }

  // Điều hướng về trang login
  goToLogin(): void {
    this.router.navigate([ApiUrls.Authentication.LOG_IN]);
  }
}
