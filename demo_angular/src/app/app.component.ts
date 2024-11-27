import { Component} from '@angular/core';
import { AuthService } from './service/auth/auth.service';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('fadeOut', [
      transition(':leave', [
        style({ opacity: 1, transform: 'scale(1)' }),
        animate('0.5s ease-out', style({ opacity: 0, transform: 'scale(0.9)' }))
      ])
    ]),
    trigger('listAnimation', [
      transition(':leave', [
        style({ opacity: 1, transform: 'translateX(0)' }),
        animate('0.5s ease-out', style({ opacity: 0, transform: 'translateX(-50%)' })),
      ]),
    ]),
  ]
})
export class AppComponent {
  constructor(public authService: AuthService
  ) { }
  title = 'demo_angular';
}
