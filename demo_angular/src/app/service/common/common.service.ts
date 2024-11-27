import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PermissionName } from '../../constant/api.const.urls';
import { RouterUrl } from '../../constant/app.const.router';
import { AuthService } from '../auth/auth.service';
import { BaseService } from '../base/base.service';
import { ErrorHandleService } from '../error-handle/error-handle.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';

@Injectable({
  providedIn: 'root'
})
export class CommonService {
  getPermission(permission: string) {
    return this.auth.getPermission(this.functionName!, permission);
  }

  public functionName: string | undefined;

  public routerLinks = RouterUrl;
  public permissionName = PermissionName;


  constructor(
    public base: BaseService,
    public errorHandle: ErrorHandleService,
    public snackBar: SnackBarService,
    public auth: AuthService,
    public router: Router
  ) {
  }

  setFunctionName(route: ActivatedRoute) {
    const path = route?.routeConfig?.path;
    this.functionName = this.auth.getFunction(path);
    if (!this.functionName) {
      this.router.navigate([RouterUrl.NOT_FOUND]);
    }
  }

  hasPermission(permissionName: string):boolean{
    return this.auth.hasPermission(this.functionName!, permissionName)
  }

  hasFunction(feRoute: string): boolean{
    return this.auth.hasFunction(feRoute);
  }

  hasRole(roleName: string){
    return this.auth.hasRole(roleName);
  }


}
