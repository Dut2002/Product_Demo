export interface SupplierInfor{
  id: number
  name: string
  contact: string
  address: string
  phone: string
  email: string
  website: string
}

export interface SupplierFilter{
  name: string|null
  contact: string|null
  address: string|null
  phone: string|null
  email: string|null
  website: string|null
  pageNum: number;
  pageSize: number;
}
