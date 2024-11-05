import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Function } from '../../../model/function';

@Component({
  selector: 'app-function-modal',
  templateUrl: './function-modal.component.html',
  styleUrl: './function-modal.component.scss'
})
export class FunctionModalComponent implements OnInit{

  @Input() isLoading = false;
  @Input() title = '';
  @Input() function!: Function;
  save!: Function;
  @Output() addEvent = new EventEmitter<Function>();
  @Output() updateEvent = new EventEmitter<Function>();
  @Output() closeEvent = new EventEmitter<void>();

  ngOnInit(): void {
      this.save = {...this.function};
  }

  saveFunction(form: NgForm) {
    if (!form.valid) {
      form.control.markAllAsTouched();
    } else {
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

  closeModal() {
    this.closeEvent.emit()
  }

}
