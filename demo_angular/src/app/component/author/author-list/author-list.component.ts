import { Component, OnInit } from '@angular/core';
import { Author } from '../../../model/author';

const authorData = [
  {
    id: 1,
    firstName: "Flora",
    lastName: "Twell",
    email: "ftwell0@phoca.cz",
    gender: "Female",
    ipAddress: "99.180.237.33",
  },
  {
    id: 2,
    firstName: "Priscella",
    lastName: "Signe",
    email: "psigne1@berkeley.edu",
    gender: "Female",
    ipAddress: "183.243.228.65",
  }
]

@Component({
  selector: 'app-author-list',
  templateUrl: './author-list.component.html',
  styleUrl: './author-list.component.scss'
})
export class AuthorListComponent implements OnInit{

  authors: Author[] = [];  // Khai báo authors là một mảng

  constructor(){}

  ngOnInit(): void {
    this.authors = authorData;
  }

  handleDelete(author: Author){
    this.authors = this.authors.filter((item) => item.id !== author.id);
  }
}
