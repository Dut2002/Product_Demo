export interface ParseOptions {
  sheets?: number[];
  headerRow?: number;
  strictMapping?: boolean;
  typeImport: ImportType;
}

export interface SheetData {
  sheetName: string;
  data: any[];
  errors?: string[];
}

export interface ParseResult {
  success: boolean;
  sheets: SheetData[];
  globalErrors: string[];
  listData: any[]
}

export interface HeaderMapping {
  excelHeader: string;     // Tên cột trong Excel
  fieldName: string;       // Tên trường trong đối tượng
  required: boolean[];
  unique: boolean[];     // Có bắt buộc không
  transform?: (value: any) => any;  // Hàm chuyển đổi giá trị nếu cần
}

export enum ImportType{
  ADD = 0,
  UPDATE = 1,
  DELETE = 2
}
