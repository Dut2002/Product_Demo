import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, Renderer2 } from '@angular/core';
import { LoginService } from '../../../service/login/login.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { Router } from '@angular/router';
import { RouterUrl } from '../../../constant/app.const.router';
import { AuthService } from '../../../service/auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  route = RouterUrl;

  constructor(private loginService: LoginService,
    private errorHandelService: ErrorHandleService,
    private router: Router,
    private el: ElementRef,
    private renderer: Renderer2,
    public authService: AuthService,
  ) {

  }

  public hasFuntions(functionRoute: string): boolean {
    return this.authService.hasFunction(functionRoute);
  }

  @Input() title = 'Title';

  isOpen = false;

  logOut() {
    this.loginService.logout().subscribe({
      next: () => {
        this.authService.endRefreshTimer();
        this.router.navigate([RouterUrl.LOG_IN]);
      },
      error: (err) => {
        this.errorHandelService.handle(err);
      }
    });
  }

  clickNav(){
    this.isOpen = !this.isOpen;
  }
}
