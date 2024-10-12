import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.scss'
})
export class ConfirmModalComponent {
  @Input() showConfirmation = false;
  @Input() title = 'Are you sure?';
  @Output() confirmEvent = new EventEmitter<void>(); // Event khi nhấn yes
  @Output() cancelEvent = new EventEmitter<void>();

  constructor(){}

  confirm(){
    this.confirmEvent.emit();
    this.closeModal();
  }

  cancel() {
    this.cancelEvent.emit();
    this.closeModal();
  }

  closeModal() {
    this.showConfirmation = false;
  }

  openModal() {
    this.showConfirmation = true;
  }
}
