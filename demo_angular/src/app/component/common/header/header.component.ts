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
    return this.authService.hasFuntion(functionRoute);
  }

  @Input() title = 'Title';
  @Input() isNavVisible: boolean = false;
  @Output() scrollEvent = new EventEmitter<number>();

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

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const scrollTop = document.documentElement.scrollTop || document.body.scrollTop || 0;

    // Kiểm tra điều kiện cuộn dưới 1000px
    if (scrollTop < 1000) {
      const backgroundSize = parseInt((scrollTop).toString()) + '%';
      const top = 50 + (scrollTop * 0.1) + '%';
      const opacity = 1 - (scrollTop * 0.003);

      this.renderer.setStyle(this.el.nativeElement.querySelector('.hero'), 'background-size', backgroundSize);
      this.renderer.setStyle(this.el.nativeElement.querySelector('.hero h1'), 'top', top);
      this.renderer.setStyle(this.el.nativeElement.querySelector('.hero h1'), 'opacity', opacity);
    }

    this.scrollEvent.emit(scrollTop);  // Phát sự kiện với vị trí cuộn

    this.updateNavBackground(scrollTop);

  }

  private updateNavBackground(scrollTop: number) {
    const navBg = this.el.nativeElement.querySelector('.nav-bg');
    if (this.isNavVisible) {
      this.renderer.removeClass(navBg, 'bg-hidden');
      this.renderer.addClass(navBg, 'bg-visible');
    } else {
      this.renderer.removeClass(navBg, 'bg-visible');
      this.renderer.addClass(navBg, 'bg-hidden');
    }
  }
}
