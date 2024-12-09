
export interface NotifyDto{
    id: number

    header: string;

    message: string;

    timestamp: Date ;

    pageRedirect: PageRedirect;

    data: string;

    read: boolean;
}

export enum PageRedirect{
  SupplierRequestManagement = 'SupplierRequestManagement',
  SupplierRequest = 'SupplierRequest'
}
