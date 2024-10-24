import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild } from '@angular/core';
import { Function } from '../../../model/function';
import { ApiStatus } from '../../../constant/api.const.urls';
import { FunctionService } from '../../../service/function/function.service';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { NgForm } from '@angular/forms';
import { ChangeAccess } from '../../../model/dto/change-acees';

@Component({
  selector: 'app-function-detail',
  templateUrl: './function-detail.component.html',
  styleUrl: './function-detail.component.scss'
})
export class FunctionDetailComponent implements OnChanges{

  @Input() open: number|null = null
  @Input() currentFunc!: Function
  name: string = ''
  showConfirmation = false;

  @Output() toggleEvent = new EventEmitter<void>()
  @Output() saveEvent = new EventEmitter<void>();

  isLoading = false;

  constructor(private functionService: FunctionService,
    private snackbarService: SnackBarService,
    private errorHandleService: ErrorHandleService
  ){}


  ngOnChanges(changes: SimpleChanges): void {
    if(changes['currentFunc']){
      this.name = this.currentFunc.name;
    }
  }

  @ViewChild('functionForm',{static : false}) functionForm!: NgForm



  onUpdateFunc() {
    if(this.functionForm.valid){
      this.isLoading = true;
      const func: Function = {
        id: this.currentFunc.id,
        name: this.currentFunc.name,
      };
      alert(func)
      this.functionService.updateFunction(func).subscribe({
        next: (response) => {
          this.snackbarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
          this.saveFunction();
          this.isLoading = false;
        },
        error: (err) => {
          this.errorHandleService.handle(err);
          this.isLoading = false;
        }
      })
    }else{
      this.functionForm.control.markAllAsTouched();
    }
  }

  onChangeAccess(){
    // const roleAccess: ChangeAccess = {
    //   id: this.currentFunc.id,
    //   roleAccesses: this.currentFunc.roleAccesses,
    // }
    // this.functionService.changeFunction(roleAccess).subscribe({
    //   next: (response) => {
    //     this.snackbarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
    //     this.saveFunction();
    //     this.isLoading = false;
    //   },
    //   error: (err) => {
    //     this.errorHandleService.handle(err);
    //     this.isLoading = false;
    //   }
    // })

  }


  closeConfirm(){
    this.showConfirmation = false;
  }

  deleteConfirm(){
    this.showConfirmation = true;
  }

  deleteFunction(){
    this.functionService.deleteFunction(this.currentFunc.id!).subscribe({
      next: (response) => {
        this.snackbarService.show(null, response.content, ApiStatus.SUCCESS, 5000);
        this.saveFunction();
        this.closeConfirm()
      },
      error: (err) => {
        this.errorHandleService.handle(err);
      }
    })
  }

  toggle() {
    this.toggleEvent.emit()
  }

  saveFunction() {
    this.saveEvent.emit();
  }
}
