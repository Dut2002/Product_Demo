import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ApiStatus } from '../../constant/api.const.urls';
import { RouterUrl } from '../../constant/app.const.router';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandleService {

  constructor(private snackBarService: SnackBarService,
    private router: Router,
  ) { }

  handle(error: any): void {
    if (error.status === 401) {
      this.snackBarService.show('Unauthorized access.', 'You do not have permission to access this resource.', ApiStatus.ERROR, 5000);
    } else if (error.status === 400) {
      this.snackBarService.show(error.error.title, error.error.content || 'Invalid input. Please check your data.', ApiStatus.ERROR, 5000);
    } else if (error.status === 404) {
      this.snackBarService.show(error.error.title || "Method Not Found", error.error.content || 'The requested method does not exist.', ApiStatus.ERROR, 5000);
      this.router.navigate([RouterUrl.NOT_FOUND]);
    } else if (error.status === 500) {
      this.snackBarService.show("Server Error", 'An unexpected error occurred on the server.', ApiStatus.ERROR, 5000);
    } else {
      this.snackBarService.show("Internal Error", 'An internal error occurred.', ApiStatus.ERROR, 5000);
    }
  }

  show(tilte: string | null, message: string): void {
    this.snackBarService.show(tilte, message, ApiStatus.ERROR, 5000);
  }
}
