import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FunctionService } from '../../../service/function/function.service';
import { ApiStatus } from '../../../constant/api.const.urls';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { finalize } from 'rxjs';
import { Function } from '../../../model/function';

@Component({
  selector: 'app-function-modal',
  templateUrl: './function-modal.component.html',
  styleUrl: './function-modal.component.scss'
})
export class FunctionModalComponent implements OnInit{

  isLoading = false;

  @Input() function!: Function;
  save!: Function;
  @Output() addEvent = new EventEmitter();
  @Output() updateEvent = new EventEmitter<Function>();

  @Output() closeEvent = new EventEmitter<void>();

  constructor(private functionService: FunctionService,
    private snackBarService: SnackBarService,
    private errorHandleService: ErrorHandleService,
  ) {
  }

  ngOnInit(): void {
      this.save = {...this.function};
  }

  saveFunction(form: NgForm) {
    if (!form.valid) {
      form.control.markAllAsTouched();
    } else {
      this.isLoading = true;
      if (this.function.id) {
        this.functionService.updateFunction(this.function)
          .pipe(finalize(() => {
            this.isLoading = false; // Sẽ chạy dù thành công hay gặp lỗi
          }))
          .subscribe({
            next: (response) => {
              this.snackBarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
              this.updateEvent.emit(this.function);
            },
            error: (error) => {
              this.errorHandleService.handle(error);
            }
          })
      } else {
        this.functionService.addFunction(this.function)
          .pipe(finalize(() => {
            this.isLoading = false; // Sẽ chạy dù thành công hay gặp lỗi
          }))
          .subscribe({
            next: (response) => {
              this.snackBarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
              this.addEvent.emit();
            },
            error: (error) => {
              this.errorHandleService.handle(error);
            }
          })
      }
    }
  }

  resetModal(form: NgForm){
    this.function = {...this.save}
    form.control.markAsUntouched();
  }

  closeModal() {
    this.closeEvent.emit()
  }

}
