export interface Voucher {
  id: number;
  code: string;
  description: string;
  discountAmount: number;
  discountPercent: number;
  startDate: Date;
  endDate: Date;
  minimumOrderValue: number;
  maximumDiscount: number;
}
