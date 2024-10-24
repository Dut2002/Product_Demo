import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Role } from '../../../model/role';
import { NgForm } from '@angular/forms';
import { RoleService } from '../../../service/role/role.service';
import { SnackBarService } from '../../../service/snack-bar/snack-bar.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { ApiStatus } from '../../../constant/api.const.urls';
import { finalize } from 'rxjs';
import { OnSameUrlNavigation } from '@angular/router';

@Component({
  selector: 'app-role-modal',
  templateUrl: './role-modal.component.html',
  styleUrl: './role-modal.component.scss'
})
export class RoleModalComponent implements OnInit {

  isLoading = false;
  save!: Role
  @Input() title = '';
  @Input() role!: Role
  @Output() addEvent = new EventEmitter();
  @Output() updateEvent = new EventEmitter<Role>();
  @Output() closeEvent = new EventEmitter();

  constructor(private roleService: RoleService,
    private snackBarService: SnackBarService,
    private errorHandle: ErrorHandleService
  ){

  }

  ngOnInit(): void {
    this.save = {...this.role}
  }

  saveRole(form: NgForm){
    if(form.valid){
      this.isLoading = true;
      if(this.role.id){
        this.roleService.updateRole(this.role).pipe(
          finalize(()=>{
            this.isLoading = false;
          }))
        .subscribe({
          next: res => {
            this.snackBarService.show(null, res.content, ApiStatus.SUCCESS, 5000)
            this.updateEvent.emit(this.role);
            this.closeModal();
          },
          error: err => this.errorHandle.handle(err),
        })
      }else{
        this.roleService.addRole(this.role).pipe(
          finalize(()=>{
            this.isLoading = false;
          }))
        .subscribe({
          next: res => {
            this.snackBarService.show(null, res.content, ApiStatus.SUCCESS, 5000)
            this.addEvent.emit();
            this.closeModal();
          },
          error: err => this.errorHandle.handle(err),
        })
      }
    }else{
      form.control.markAllAsTouched();
    }
  }

  resetModal(form: NgForm){
    form.control.markAsUntouched();
    this.role = {...this.save}
  }

  closeModal(){
    this.closeEvent.emit();
  }
}
