import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Role } from '../../../model/role';

@Component({
  selector: 'app-role-modal',
  templateUrl: './role-modal.component.html',
  styleUrl: './role-modal.component.scss'
})
export class RoleModalComponent implements OnInit {

  save!: Role
  @Input() title = '';
  @Input() isLoading = false;
  @Input() role!: Role
  @Output() addEvent = new EventEmitter<Role>();
  @Output() updateEvent = new EventEmitter<Role>();
  @Output() closeEvent = new EventEmitter();

  constructor(){
  }

  ngOnInit(): void {
    this.save = {...this.role}
  }

  saveRole(form: NgForm){
    if(form.valid){
      this.isLoading = true;
      if(this.role.id){
        this.updateEvent.emit(this.role);
      }else{
        this.addEvent.emit(this.role);
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
