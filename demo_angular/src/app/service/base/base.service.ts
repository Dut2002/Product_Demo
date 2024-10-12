import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApiHeaders, env} from '../../constant/api.const.urls'


@Injectable({
  providedIn: 'root'
})
export class BaseService {

  constructor(private http: HttpClient) { }

  // Gọi API GET
  get(endpoint: string, params?: HttpParams): Observable<any> {
    return this.http.get(`${env+endpoint}`, { params });
  }

  // Gọi API GET
  get_body(endpoint: string, data: any): Observable<any> {
    return this.http.post(`${env+endpoint}`, data);
  }

  // Gọi API POST
  post(endpoint: string, data: any): Observable<any> {
    return this.http.post(`${env+endpoint}`, data, {
      headers: new HttpHeaders(ApiHeaders.HEADER_DEFAULT)
    });
  }

  // Gọi API PUT
  put(endpoint: string, data: any): Observable<any> {
    return this.http.put(`${env+endpoint}`, data, {
      headers: new HttpHeaders(ApiHeaders.HEADER_DEFAULT)
    });
  }

  // Gọi API DELETE
  delete(endpoint: string, params?: HttpParams): Observable<any> {
    return this.http.delete(`${env+endpoint}`, {params});
  }

  uploadFile(endpoint: string, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${env+endpoint}`, formData);
  }

  export(endpoint: string, params?: HttpParams): Observable<Blob> {
    const options = {
      headers: new HttpHeaders(ApiHeaders.HEADER_DEFAULT),
      params: params,
      responseType: 'blob' as 'json' // 'blob' cần phải được định nghĩa là 'json' để TypeScript nhận biết
    };

    return this.http.get<Blob>(`${env+endpoint}`, options);
  }

  importFile(endpoint: string, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${env+endpoint}`, formData);
  }

}
