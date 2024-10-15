import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { ApiHeaders } from '../../constant/api.const.urls';
import { LoginService } from '../login/login.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private loginService: LoginService) { }

  isLoggedIn() {
    return !this.isTokenExpired(this.getRefresh());
  }

  isAuthenticated(authority: string): boolean {
    return this.hasAuthority(authority) && !this.isTokenExpired(this.getToken());
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

  getAuthority(): string[] {
    const token = this.getToken();
    if (!token) return [];
    const decodedToken = this.decodeToken(token);
    const authorities = decodedToken.authorities || [];
    return authorities;
  }

  getRole(): string {
    const token = this.getToken();
    if (!token) return '';
    const decodedToken = this.decodeToken(token);
    const role = decodedToken.role || '';
    return role;
  }

  public hasAuthority(authority: string): boolean {
    const token = this.getToken();
    if (!token) return false;

    const decodedToken = this.decodeToken(token);
    const authorities = decodedToken.authorities || [];
    return authorities.includes(authority);
  }

  public isTokenExpired(token: string | null): boolean {
    if (!token) return true;

    const decoded = this.decodeToken(token);
    const expirationDate = decoded.exp * 1000; // Chuyển sang milliseconds
    return Date.now() >= expirationDate;
  }

  public sendRefreshToken():Promise<boolean> {
    return new Promise((resolve) => {
      const refresh = this.getRefresh();
      if (!refresh) {
        localStorage.clear();
        resolve(false);
        return;
      }
      if (this.isTokenExpired(refresh)){
        resolve(false)
        return;
        }
      this.loginService.sendRefreshToken(refresh).subscribe({
        next: (response) => {
          localStorage.setItem(ApiHeaders.TOKEN_KEY, response.token);
          localStorage.setItem(ApiHeaders.REFRESH_KEY, response.refreshToken)
          localStorage.setItem(ApiHeaders.ROLE_KEY, response.role[0])
          resolve(true);
          return;
        },
        error: () => {
          localStorage.clear();
          resolve(false)
          return;
        },
      });
    });
  }




  clearLocalStorage() {
    localStorage.clear();
    sessionStorage.clear();
  };
}
