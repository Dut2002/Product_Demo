import { Component, ErrorHandler, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import SockJS from "sockjs-client";
import Stomp, { Client } from "stompjs";
import { PermissionName } from '../../../constant/api.const.urls';
import { RouterUrl } from '../../../constant/app.const.router';
import { NotifyDto, PageRedirect } from '../../../model/header/notify-dto';
import { LoginService } from '../../../service/login/login.service';
import { AuthService } from '../../../service/auth/auth.service';
import { ErrorHandleService } from '../../../service/error-handle/error-handle.service';
import { BaseService } from '../../../service/base/base.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  functionName: string | undefined

  socketClient: Client | null = null;




  @Input() title = 'Title';

  isOpen = false;
  isNotifyOpen = false;
  hasUnread = false;
  last = true;
  pageNum = 1;

  notifies: NotifyDto[] = [];
  routerLinks = RouterUrl;

  constructor(private loginService: LoginService,
    public auth: AuthService,
    public base: BaseService,
    private errorHandle: ErrorHandleService,
    private router: Router,
    private toastService: ToastrService,
  ) {
  }

  ngOnInit(): void {

    this.functionName = this.auth.getFunction(this.routerLinks.VIEW_NOTIFICATION);
    if (!this.functionName) this.logOut();

    if (!this.auth.isTokenExpired())
      // Kết nối WebSocket khi component được khởi tạo
      this.connectToWebSocket();

    this.hasUnreadNotify();
  }


  public hasFuntions(functionRoute: string): boolean {
    return this.auth.hasFunction(functionRoute);
  }



  logOut() {
    this.loginService.logout().subscribe({
      next: () => {
        this.auth.endRefreshTimer();
        this.router.navigate([RouterUrl.LOG_IN]);
      },
      error: (err) => {
        this.errorHandle.handle(err);
      }
    });
  }

  openNotify() {
    this.loadNotifies();
    this.isNotifyOpen = true;
  }

  clickNav() {
    this.isOpen = !this.isOpen;
  }

  private stompClient: any;
  notifications: string[] = [];



  ngOnDestroy(): void {
    // Đóng kết nối khi component bị huỷ
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }

  connectToWebSocket() {
    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = Stomp.over(socket);

    // this.stompClient.reconnect_delay = 5000;

    this.stompClient.connect(
      { 'Authorization': 'Bearer ' + this.auth.getToken() }, // Loại bỏ dấu : sau Authorization
      () => {
        this.stompClient.subscribe(
          `/user/${this.auth.getUsername()}/notification`,
          (message: any) => {
            this.handelNotify(message);
            if (this.isNotifyOpen) {
              this.loadNotifies();
            }
            this.hasUnreadNotify();
          }
        );
        let roles: string[] = this.auth.getRoles();
        roles.forEach(role => {
          this.stompClient.subscribe(
            `/topic/role/${role}/notification/`,
            (message: any) => {
              // Xử lý thông báo ở đây
              console.log(message);

              if (this.isNotifyOpen) {
                this.loadNotifies();
              }
              this.hasUnreadNotify();
            }
          );
        })
        // console.log('WebSocket connected');

      },
      (error: any) => {
        // Thêm error callback để bắt lỗi kết nối
        // console.error('WebSocket connection error', error);
      }
    );
  }

  handelNotify(message: any) {
    const notify = JSON.parse(message.body);
    if (notify) {
      this.toastService.info(notify.message, notify.header);
    }
  }

  markReadAll() {
    const endpoint = this.auth.getPermission(this.functionName!, PermissionName.ViewNotification.CHECK_READ_ALL)
    if (!endpoint) {
      this.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.base.put(endpoint).subscribe({
      next: () => {
        this.notifies.forEach(notify => {
          notify.read = true;
        })
        this.hasUnreadNotify();
      },
      error: (err) => {
        this.errorHandle.handle(err);
      }
    })
  }

  clickNotify(notify: NotifyDto) {
    if (!notify.read) {
      const endpoint = this.auth.getPermission(this.functionName!, PermissionName.ViewNotification.CHECK_READ)
      if (!endpoint) {
        this.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
        return;
      }
      const body = {
        notifyId: notify.id
      }
      this.base.put(endpoint, body)
        .subscribe({
          next: () => {
            const targetNotify = this.notifies.find(n => n.id === notify.id);
            if (targetNotify) {
              targetNotify.read = true;
            }
            this.hasUnreadNotify();
          },
          error: (err) => {
            this.errorHandle.handle(err);
          }
        })
    }
    if (notify.pageRedirect) {
      const route = this.mapPageRedirectToRoute(notify.pageRedirect);
      if (route) {
        this.router.navigate([route]);
      } else {
        this.errorHandle.show('Navigation Error', 'The page associated with this notification could not be found.');
      }
    }
    this.clickNot()
  }

  /**
   * Map `PageRedirect` enum values to `RouterUrl` paths.
   */
  private mapPageRedirectToRoute(pageRedirect: PageRedirect): string | null {
    switch (pageRedirect) {
      case PageRedirect.SupplierRequest:
        return RouterUrl.MY_SUPPLIER;
      case PageRedirect.SupplierRequestManagement:
        return RouterUrl.SUPPLIER_APPROVAL;
      default:
        return null; // Trả về null nếu không khớp
    }
  }

  clickNot() {
    this.isNotifyOpen = false;
  }

  hasUnreadNotify() {

    const endpoint = this.auth.getPermission(this.functionName!, PermissionName.ViewNotification.HAS_UNREAD_NOTIFY)
    if (!endpoint) {
      this.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }
    this.base.get(endpoint).subscribe({
      next: (response) => {
        this.hasUnread = response
      },
      error: (err) => {
        this.errorHandle.handle(err);
      }
    })
  }

  loadNotifies(isLoadMore: boolean = false) {
    const endpoint = this.auth.getPermission(this.functionName!, PermissionName.ViewNotification.GET_NOTIFIES);
    if (!endpoint) {
      this.errorHandle.show('Unauthorized access.', 'You do not have permission to access this resource!');
      return;
    }

    // Nếu là loadMore, tăng số trang, ngược lại reset danh sách
    this.pageNum = isLoadMore ? this.pageNum + 1 : 1;

    const request = {
      pageRedirect: null,
      pageNum: this.pageNum,
      pageSize: 8,
    };

    this.base.post(endpoint, request).subscribe({
      next: (response) => {
        if (isLoadMore) {
          this.notifies = [...this.notifies, ...response.content]; // Gộp danh sách
        } else {
          this.notifies = response.content; // Tải mới danh sách
        }
        this.last = response.last; // Cập nhật trạng thái cuối
      },
      error: (err) => {
        this.errorHandle.handle(err);
      },
    });
  }
}
