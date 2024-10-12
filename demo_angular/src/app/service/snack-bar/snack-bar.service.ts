import { inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarComponent } from '../../component/common/snack-bar/snack-bar.component';

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {

  private snackBar = inject(MatSnackBar);

  show(title: string|null, message: string, type: string = 'normal' , duration: number = 3000) {
    this.snackBar.openFromComponent(SnackBarComponent, {
      duration: duration,
      data: {
        snackMessage: message,
        snackType: type,
        snackTitle: title
      },
    });
  }
}
