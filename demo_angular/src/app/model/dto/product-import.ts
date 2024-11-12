export interface ProductImport{
  id: number
  name: string
  yearMaking: number
  price: number
  quantity: number
  expireDate: Date
  categoryName: string
  supplierName: string
  success: boolean|null
}
