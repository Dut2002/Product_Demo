import { Voucher } from "../voucher"

export interface ProductDto {
  id: number
  name: string
  yearMaking: number
  price: number
  quantity: number
  expireDate: Date
  categoryId: number
  categoryName: string
  supplierId: number
  supplierName: string
  vouchers: Voucher[]
}
