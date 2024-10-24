import { Component } from '@angular/core';
import { FileService } from '../../service/file/file.service';
import { HttpErrorResponse, HttpEvent, HttpEventType } from '@angular/common/http';
import { saveAs } from 'file-saver'
@Component({
  selector: 'app-file-progress',
  templateUrl: './file-progress.component.html',
  styleUrl: './file-progress.component.scss'
})
export class FileProgressComponent {

  fileNames: string[] = []

  fileStatus = {
    status: '',
    requestType: '',
    percent: 0
  }

  constructor(private fileService: FileService) { }

  onUploadFiles(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files) {
      const files: File[] = Array.from(input.files);
      // Thực hiện xử lý với danh sách file

      const formData = new FormData();
      for (const file of files) {
        formData.append('files', file, file.name)
      }
      this.fileService.upload(formData)
        .subscribe({
          next: (event: HttpEvent<string[]>) => {
            console.log(event);
            this.resportProgress(event)
          },
          error: (error: HttpErrorResponse) => {
            console.log(error);
          },
          complete: () => {
            console.log("Upload Completed!");
          }
        });
    } else {
      console.log('No files selected');
    }
  }
  private resportProgress(event: HttpEvent<string[] | Blob>) {

    switch (event.type) {
      case HttpEventType.UploadProgress: {
        this.updateStatus(event.loaded, event.total!, 'Uploading');
        break;
      }
      case HttpEventType.DownloadProgress: {
        this.updateStatus(event.loaded, event.total!, 'Downloading');
        break;
      }
      case HttpEventType.ResponseHeader: {
        console.log('HeaderReturn');
        break;
      }
      case HttpEventType.Response: {
        if (event.body instanceof Array) {
          for (const filename of event.body) {
            this.fileNames.unshift(filename);
          }
        } else {
5
          saveAs(new File([event.body!], event.headers.get('File-Name') || 'default-file-name',
            { type: `${event.headers.get('Content-Type')}; charset=utf-8` }));

          // saveAs(
          //   new Blob([event.body!], { type: event.headers.get('Content-Type') || 'application/octet-stream' }),
          //   event.headers.get('File-Name') || 'default-file-name'
          // );
        }
        this.fileStatus.status = 'done';
        break;
      }
      default: {
        console.log(event);
      }
    }
  }

  private updateStatus(loaded: number, total: number, requestType: string) {
    this.fileStatus.status = 'progress'
    this.fileStatus.requestType = requestType;
    this.fileStatus.percent = Math.round(100 * loaded / total);

  }

  onDownloadFile(filename: string) {
    this.fileService.download(filename).subscribe({
      next: event => {
        console.log(event);
        this.resportProgress(event);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
      complete: () => {
        console.log('Download completed');
      }
    });
  }

}

