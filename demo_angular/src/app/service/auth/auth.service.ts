import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { ApiHeaders } from '../../constant/api.const.urls';
import { LoginService } from '../login/login.service';
import { catchError, from, Observable, of, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private loginService: LoginService) { }

  isLoggedIn() {
    return !this.isTokenExpired(this.getRefresh());
  }

  isAuthenticated(authority: string): boolean {
    return this.hasPermission(authority) && !this.isTokenExpired(this.getToken());
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

  public hasPermission(permission: string): boolean {
    const token = this.getToken();
    if (!token) return false;

    console.log(this.decodeToken(token));

    const decodedToken = this.decodeToken(token);
    // Lấy mảng permissions từ decodedToken
    const permissions = decodedToken.permissions || [];

    // Tạo danh sách tất cả các feEndPoint
    const feEndPoints = permissions.map((perm: any) => perm.feEndPoint);

    // Kiểm tra xem permission có trong danh sách feEndPoint không
    return feEndPoints.includes(permission);
  }

  public isTokenExpired(token: string | null): boolean {
    if (!token) return true;
    const decoded = this.decodeToken(token);
    const expirationDate = decoded.exp * 1000; // Chuyển sang milliseconds
    return Date.now() >= expirationDate;
  }

  public sendRefreshToken(): Promise<boolean> {
    return new Promise((resolve) => {
      const refresh = this.getRefresh();
      if (!refresh) {
        localStorage.clear();
        resolve(false);
        return;
      }
      if (this.isTokenExpired(refresh)) {
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


  checkAndRefreshToken(): Observable<string | null> {
    const token = this.getToken();
    if (this.isTokenExpired(token)) {
      const refresh = this.getRefresh();
      if (refresh && !this.isTokenExpired(refresh)) {
        return this.loginService.sendRefreshToken(refresh).pipe(
          switchMap((response) => {
            localStorage.setItem(ApiHeaders.TOKEN_KEY, response.token);
            localStorage.setItem(ApiHeaders.REFRESH_KEY, response.refreshToken);
            localStorage.setItem(ApiHeaders.ROLE_KEY, response.role[0]);
            return of(response.token)
          }),
          catchError(() => {
            // Nếu làm mới token thất bại, xóa localStorage và trả về null
            localStorage.clear();
            return of(null);
          })
        );
      } else {
        // Nếu refresh token đã hết hạn, trả về null
        localStorage.clear();
        return of(null);
      }
    }
    // Nếu token chưa hết hạn, trả về token hiện tại
    return of(token);
  }

  clearLocalStorage() {
    localStorage.clear();
    sessionStorage.clear();
  };
}
