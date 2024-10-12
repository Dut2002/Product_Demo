import { Component} from '@angular/core';
import { AuthService } from './service/auth/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  constructor(public authService: AuthService
  ) { }

  isNavVisible = false;

  onScroll(scrollTop: number) {
    this.isNavVisible = scrollTop >= 150; // Ví dụ, nếu cuộn đến 300px
  }
  title = 'demo_angular';
}
