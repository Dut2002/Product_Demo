export class ProductFilter {
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

  //Order
    orderCol: string | null = null;
    sortDesc: boolean = false;
  //Paging
  pageNum: number = 1;
  sizePage: number = 8;

}
