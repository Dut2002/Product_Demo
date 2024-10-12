import {Component, Inject} from '@angular/core';
import {
  MAT_SNACK_BAR_DATA,
  MatSnackBarRef,
} from '@angular/material/snack-bar';


@Component({
  selector: 'app-snack-bar',
  templateUrl: './snack-bar.component.html',
  styleUrl: './snack-bar.component.scss'
})
export class SnackBarComponent {
  snackTitle: string = 'Message'
  snackMessage: string = 'Jella';
  // snackButton: string = 'Cancel';
  snackType: string = 'normal';

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: any, // Inject dữ liệu truyền vào từ service
    public snackBarRef: MatSnackBarRef<SnackBarComponent>
  ) {
    this.snackMessage = data.snackMessage;
    // this.snackButton = data.snackButton;
    this.snackType = data.snackType || 'normal'
    this.snackTitle = data.snackTitle || 'Message'
  }
}
