import { Injectable } from '@angular/core';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { ApiStatus } from '../../constant/api.const.urls';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandleService {

  constructor(private snackBarService: SnackBarService) { }

  handle(error: any): void {
    if (error.status === 401) {
      this.snackBarService.show('Unauthorized access.', 'You do not have permission to access this resource.', ApiStatus.ERROR, 5000);
    } else if (error.status === 400) {
      this.snackBarService.show(error.error.title, error.error.content || 'Invalid input. Please check your data.', ApiStatus.ERROR, 5000);
    } else if (error.status === 404) {
      this.snackBarService.show("Method Not Found", 'The requested method does not exist.', ApiStatus.ERROR, 5000);
    } else if (error.status === 500) {
      this.snackBarService.show("Server Error", 'An unexpected error occurred on the server.', ApiStatus.ERROR, 5000);
    } else {
      this.snackBarService.show("Internal Error", 'An internal error occurred.', ApiStatus.ERROR, 5000);
    }
  }

  show(tilte: string|null, message: string): void{
    this.snackBarService.show(tilte, message, ApiStatus.ERROR, 5000);
  }
}
