import { Injectable } from '@angular/core';
import { BaseService } from '../base/base.service';
import { ApiUrls } from '../../constant/api.const.urls';
import { LoginRequest } from '../../model/dto/login-request';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private baseService: BaseService) { }

  login(loginRequest: LoginRequest):Observable<any>{
    return this.baseService.post(ApiUrls.Authentication.LOG_IN, loginRequest)
  }

  sendRefreshToken(refreshToken: string):Observable<any>{
    return this.baseService.post(ApiUrls.Authentication.CHECK_REFRESH, refreshToken)
  }

  logout():Observable<any>{
    return this.baseService.post(ApiUrls.Authentication.LOG_OUT, null)
  }
}
