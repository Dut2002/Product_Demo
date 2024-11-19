import { Component } from '@angular/core';
import { CommonService } from '../../../service/common/common.service';
import { RouterUrl } from '../../../constant/app.const.router';
import { Router } from '@angular/router';
import { AssetsUrl } from '../../../constant/assets.const.urls';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  src = AssetsUrl.BANNER_IMAGE

  constructor(common: CommonService, private router: Router,
  ){
    if(common.auth.isTokenExpired()){
      common.auth.clearLocalStorage();
      this.router.navigate([RouterUrl.HOME]);
    }
  }
}
