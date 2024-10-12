import { Component, OnInit } from '@angular/core';
import { ApiHeaders } from '../../../constant/api.const.urls';
import { RouterUrl } from '../../../constant/app.const.router';
import { Role } from '../../../constant/app.const.role';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{

  constructor(private router: Router,
  ){}

  ngOnInit(): void {
    const role = localStorage.getItem(ApiHeaders.ROLE_KEY);
    switch(role){
      case Role.USER:{
        this.router.navigate([RouterUrl.GET_PRODUCTS.path]);
        break;
      }
      case Role.ADMIN:{
        this.router.navigate([RouterUrl.GET_PRODUCTS.path]);
        break;
      }
      case Role.SYS_ADMIN:{
        this.router.navigate([RouterUrl.GET_FUNCTIONS.path]);
        break;
      }
      default:{
        this.router.navigate([RouterUrl.LOG_IN.path]);
      }
    }
  }
}
