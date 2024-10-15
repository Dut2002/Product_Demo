import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.scss'
})
export class ConfirmModalComponent {
  @Input() title = 'Are you sure?';
  @Output() confirmEvent = new EventEmitter<void>(); // Event khi nhấn yes
  @Output() closeEvent = new EventEmitter<void>(); // Event khi nhấn yes


  constructor(){}

  confirm(){
    this.confirmEvent.emit();
    this.cancel();
  }

  cancel() {
    this.closeEvent.emit();
  }
}
