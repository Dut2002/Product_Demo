export interface MyRequest {
  id: number;
  data: string;
  status: ApprovalStatus;
  createdAt: Date;
  updatedAt: Date;
  note: string;
}

export interface RequestDto {
  id: number;
  data: any;
  approvalType: ApprovalType
  status: ApprovalStatus;
  createdAt: Date;
  updatedAt: Date;
  note: string;
  requesterId: number;
  fullName: string;
}

export interface RequestFilter {
  approvalType: ApprovalType | undefined
  status: ApprovalStatus | undefined;
  createdStart: Date | undefined;
  createdEnd: Date | undefined;
  updatedStart: Date | undefined;
  updatedEnd: Date | undefined;
  note: string | undefined;
  requesterId: number | undefined;

  pageNum: number;
  pageSize: number;
}

export enum ApprovalType {
  SUPPLIER_REGISTRATION = "SUPPLIER_REGISTRATION",
}

export enum ApprovalStatus {
  APPROVED = "APPROVED",
  REJECTED = "REJECTED",
  PENDING = "PENDING",
  CANCELED = "CANCELED"
}

export interface OpenSupplierReq {
  name: string
  contact: string;
  address: string;
  phone: string;
  email: string;
  website: string;
}
