import { Injectable } from '@angular/core';
import { BaseService } from '../base/base.service';
import { ApiUrls } from '../../constant/api.const.urls';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private baseService: BaseService) { }

  login(loginForm: any){
    return this.baseService.post(ApiUrls.Authentication.LOG_IN, loginForm)
  }

  sendRefreshToken(refreshToken: string|''){
    return this.baseService.post(ApiUrls.Authentication.CHECK_REFRESH, refreshToken)
  }

  logout(){
    return this.baseService.post(ApiUrls.Authentication.LOG_OUT, null)
  }
}
