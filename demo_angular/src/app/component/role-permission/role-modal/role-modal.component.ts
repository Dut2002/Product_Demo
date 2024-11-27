import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Role } from '../../../model/role';

@Component({
  selector: 'app-role-modal',
  templateUrl: './role-modal.component.html',
  styleUrl: './role-modal.component.scss'
})
export class RoleModalComponent implements OnChanges {

  save: Role = {} as Role
  @Input() title = '';
  showModal = false;
  isLoading = false;
  @Input() role!: Role
  @Output() addEvent = new EventEmitter<Role>();
  @Output() updateEvent = new EventEmitter<Role>();

  constructor(){
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['role']?.currentValue !== changes['role']?.previousValue) {
      this.save = { ...this.role }; // Sao chép giá trị từ role để tránh thay đổi trực tiếp
    }
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
    this.showModal = false;
  }
}
