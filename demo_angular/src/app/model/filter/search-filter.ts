import { AccountStatus } from "../account-management/account-status";

export class SearchFilter{
    pageNum: number = 1;
    pageSize: number = 8;
}

export class ProductFilter extends SearchFilter{
  name: string | null = null;
  yearMaking: number | null = null;

  minPrice: number | null = null;
  maxPrice: number | null = null;
  minQuantity: number | null = null;
  maxQuantity: number | null = null;
  startExpireDate: Date | null = null;
  endExpireDate: Date | null = null;

  categoryId: number | null = null;
  supplierId: number | null = null;
  voucherCode: string | null = null;
  customerId: number | null = null;

  orderCol: string | null = null;
  sortDesc: boolean = false;
}

export class SupplierFilter extends SearchFilter{
  name: string|null = null
  contact: string|null = null
  address: string|null = null
  phone: string|null = null
  email: string|null = null
  website: string|null = null
}

export class AccountFilter extends SearchFilter{
  fullName: string|null = null
  username: string|null = null
  email: string|null = null
  status: AccountStatus |null = null
  roleId: number | null = null
}

