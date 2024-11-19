import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { ApiHeaders } from '../../constant/api.const.urls';
import { LoginService } from '../login/login.service';
import { catchError, Observable, of, Subscription, switchMap, timer } from 'rxjs';
import { ErrorHandleService } from '../error-handle/error-handle.service';
import { FunctionDto } from '../../model/dto/function-dto';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private refreshTokenTimer: Subscription | null = null;
  private readonly refresheInterval = 10 * 60 * 1000;

  constructor(private loginService: LoginService, private errorHandelService: ErrorHandleService) { }

  startRefreshTimer() {
    if (this.refreshTokenTimer) {
      this.refreshTokenTimer.unsubscribe();
    }
    const refresh = this.getRefresh();
    if (!refresh) {
      this.clearLocalStorage;
      this.refreshTokenTimer = null;
      return;
    }
    this.refreshTokenTimer = timer(this.refresheInterval)
      .pipe(
        switchMap(() => this.loginService.sendRefreshToken(refresh)),
        catchError(err => {
          this.errorHandelService.handle(err);
          this.clearLocalStorage();
          return of(null);
        })
      ).subscribe((response) => {
        localStorage.setItem(ApiHeaders.TOKEN_KEY, response.token);
        localStorage.setItem(ApiHeaders.REFRESH_KEY, response.refreshToken)
        localStorage.setItem(ApiHeaders.ROLE_KEY, response.roles)

        this.startRefreshTimer();
      })
  }

  endRefreshTimer() {
    if (this.refreshTokenTimer) {
      this.refreshTokenTimer.unsubscribe();
      this.refreshTokenTimer = null;
    }
    this.clearLocalStorage();
  }

  getToken(): string | null {
    return localStorage.getItem(ApiHeaders.TOKEN_KEY);
  }

  getRefresh(): string | null {
    return localStorage.getItem(ApiHeaders.REFRESH_KEY);
  }

  decodeToken(token: string): any {
    return jwtDecode(token);
  }

  getRoles(): string[] {
    const token = this.getToken();
    if (!token) return [];

    const decodedToken = this.decodeToken(token);
    // Kiểm tra xem `roles` có phải là một mảng và chứa các chuỗi không
    const roles: any[] = decodedToken.roles;
    return Array.isArray(roles) && roles.every(role => typeof role === 'string') ? roles : [];
  }

  public getFunction(feRoute?: string): string | undefined {
    const token = feRoute ? this.getToken() : null;
    const functions = token ? this.decodeToken(token).functions || [] : [];
    return functions.find((func: any) => func.feRoute === feRoute)?.name;
  }

  public hasFunction(feRoute: string): boolean {
    const token = this.getToken();
    if(!token) return false;
    const functions = this.decodeToken(token).functions;
    return functions.some((func: any) => func.feRoute === feRoute);
  }



  public getPermission(functionName: string, permissionName: string): string | null {
    const token = this.getToken();
    if (!token) return null;
    const decodedToken = this.decodeToken(token);
    const functionInfo = decodedToken.functions.find((f: any) => f.name === functionName);
    // Tìm PermissionInfo theo permissionName
    const permission = functionInfo.permissions.find((p: any) => p.name === permissionName);
    return permission ? permission.beEndPoint : null;
  }

  public hasPermission(functionName: string, permissionName: string): boolean {
    const token = this.getToken();
    if (!token) return false;
    const decodedToken = this.decodeToken(token);
    const functionInfo = decodedToken.functions.find((f: any) => f.name === functionName);
    if (!functionInfo) return false;
    return functionInfo.permissions.some((p: any) => p.name === permissionName);;
  }

  public isTokenExpired(): boolean {
    const token = this.getToken()
    if (!token) return true;
    const decoded = this.decodeToken(token);
    const expirationDate = decoded.exp * 1000; // Chuyển sang milliseconds
    return Date.now() >= expirationDate;
  }

  public isRefreshExpired(): boolean {
    const refresh = this.getToken()
    if (!refresh) return true;
    const decoded = this.decodeToken(refresh);
    const expirationDate = decoded.exp * 1000; // Chuyển sang milliseconds
    return Date.now() >= expirationDate;
  }

  clearLocalStorage() {
    localStorage.clear();
    sessionStorage.clear();
    this.refreshTokenTimer?.unsubscribe();
    this.refreshTokenTimer = null;
  };
}
