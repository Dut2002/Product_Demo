import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiHeaders } from '../constant/api.const.urls';

export function authenInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  // Lấy token từ localStorage
  const token = localStorage.getItem(ApiHeaders.TOKEN_KEY);

  // Kiểm tra xem token có tồn tại hay không
  if (token) {


    // Clone request và thêm Authorization header
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  // Nếu không có token, chuyển request gốc
  return next(req);
}
