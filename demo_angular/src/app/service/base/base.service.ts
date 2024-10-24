import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
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

  uploadFile(endpoint: string, formData: FormData): Observable<HttpEvent<string[]>> {
    return this.http.post<string[]>(`${env+endpoint}`, formData, {
      reportProgress:true,
      observe: 'events'
    });
  }

  downloadFile(endpoint: string, name: string):Observable<HttpEvent<Blob>>{
    return this.http.get(`${env+endpoint}/${name}`, {
      reportProgress: true,
      observe: 'events',
      responseType: 'blob'
    })
  }

  export(endpoint: string, params?: HttpParams):Observable<HttpResponse<Blob>> {
    return this.http.get<Blob>(`${env + endpoint}`,
      {
        params: params,
        reportProgress: true,
        observe: 'response' as const,
        responseType: 'blob' as 'json' //      }
      }
    );
  }

  importFile(endpoint: string, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${env+endpoint}`, formData);
  }

}
