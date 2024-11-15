export interface MyRequest{
  id: number;
  data: string;
  status: ApprovalStatus;
  createdAt: Date;
  updatedAt: Date;
  note: string;
}

export enum ApprovalStatus{
  APPROVED = "APPROVED",
  REJECTED = "REJECTED",
  PENDING = "PENDING",
  CANCELED = "CANCELED"
}

export interface OpenSupplierReq{
  name: string
  contact: string ;
  address: string ;
  phone: string ;
  email: string ;
  website: string ;
}
