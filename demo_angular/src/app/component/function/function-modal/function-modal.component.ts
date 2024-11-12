import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Function } from '../../../model/function';

@Component({
  selector: 'app-function-modal',
  templateUrl: './function-modal.component.html',
  styleUrl: './function-modal.component.scss'
})
export class FunctionModalComponent implements OnInit{

  showModal = false
  isLoading = false;
  @Input() title = '';
  @Input() function!: Function;
  private save!: Function;

  @Output() addEvent = new EventEmitter<Function>();
  @Output() updateEvent = new EventEmitter<Function>();

  ngOnInit(): void {
      this.save = {...this.function};
  }

  saveFunction(form: NgForm) {
    if (!form.valid) {
      form.control.markAllAsTouched();
    } else {
      this.isLoading = true;
      if (this.function.id) {
        this.updateEvent.emit(this.function);
      } else {
        this.addEvent.emit(this.function);
      }
    }
  }

  resetModal(form: NgForm){
    this.function = {...this.save}
    form.control.markAsUntouched();
  }

}
