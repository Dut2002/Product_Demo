import { Injectable } from '@angular/core';
import { BaseService } from '../base/base.service';
import { ApiUrls } from '../../constant/api.const.urls';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private baseService: BaseService) { }

  //function upload

  upload(formData: FormData){
    return this.baseService.uploadFile(ApiUrls.File.UPLOAD_FILE, formData)
  }
  //function dơnload
  download(name: string){
    return this.baseService.downloadFile(ApiUrls.File.DOWNLOAD_FILE, name);
  }
}
