import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Author } from '../../../model/author';

@Component({
  selector: 'app-author-detail',
  templateUrl: './author-detail.component.html',
  styleUrl: './author-detail.component.scss'
})
export class AuthorDetailComponent implements OnInit{
  @Input() author: Author|undefined
  @Output() deleteAuthor = new EventEmitter<Author>();
  constructor(){}

  ngOnInit(): void {
  }

  handleDelete(){
    this.deleteAuthor.emit(this.author)
  }

}
