import * as ExcelJS from 'exceljs';
import { HeaderMapping, ParseOptions, ParseResult, SheetData } from "../model/import/import-dto";
import { ValidationConfig } from "../model/import/validation-dto";
import { ValidationService } from "./validate.service";

export class ExcelParse {

  headerMappings: HeaderMapping[];
  validationService!: ValidationService;
  validationConfig: ValidationConfig;
  parseOptions: ParseOptions;

  constructor(headerMappings: HeaderMapping[], validationConfig: ValidationConfig, options: ParseOptions) {
    this.headerMappings = headerMappings;
    this.validationConfig = validationConfig;
    this.parseOptions = options;
  }

  // public listData: any[] = [];

  validateHeaders(worksheet: ExcelJS.Worksheet, headerRowNumber: number, options: ParseOptions): {
    isValid: boolean;
    headerMap: Map<number, HeaderMapping>;
    errors: string[];
  } {
    const errors: string[] = [];
    const headerMap = new Map<number, HeaderMapping>();
    const headerRow = worksheet.getRow(headerRowNumber);

    const excelHeaders = new Map<number, string>();
    headerRow.eachCell({ includeEmpty: false }, (cell, colNumber) => {
      excelHeaders.set(colNumber, cell.value?.toString().trim() || '');
    });

    this.headerMappings.forEach(mapping => {
      let found = false;
      excelHeaders.forEach((headerValue, colNumber) => {
        if (headerValue == mapping.excelHeader) {
          headerMap.set(colNumber, mapping);
          found = true;
        }

      })
      if (!found && (mapping.required[options.typeImport] || options.strictMapping)) {
        errors.push(`Không tìm thấy cột bắt buộc "${mapping.excelHeader}" `);
      }
    });
    return {
      isValid: errors.length === 0,
      headerMap,
      errors
    };
  }
  // validateCell(fieldName: string, value: any, rowNumber: number, sheetName: string): ValidationResult{
  // }

  private parseSheet(sheet: ExcelJS.Worksheet): SheetData {
    const headerRow = this.parseOptions.headerRow || 1;
    const errors: string[] = [];
    const jsonData: any[] = [];

    const { isValid, headerMap, errors: headerErrors } = this.validateHeaders(sheet, headerRow, this.parseOptions);



    if (!isValid) {
      return {
        sheetName: sheet.name,
        data: [],
        errors: [`Sheet "${sheet.name}": ${headerErrors}`],
      };
    }

    sheet.eachRow({ includeEmpty: false }, (row: ExcelJS.Row, rowNumber) => {
      if (rowNumber <= headerRow) return;
      const data: any = {};
      let rowValid = true;
      let hasValue = false;
      const errorRow: string[] = []
      headerMap.forEach((mapping, colNumber) => {

        const cell = row.getCell(colNumber);
        let cellValue = cell.value;

        if (cellValue != null) {
          hasValue = true;
          if (mapping.transform) {
            try {
              cellValue = mapping.transform(cellValue);
            } catch (err) {
              errorRow.push(`Dòng ${rowNumber}: Lỗi chuyển đổi giá trị cho trường ${mapping.fieldName}: ${err}`);
              rowValid = false;
              return;
            }
          }
        }
        const validation = this.validationService.validateField(mapping.fieldName, cellValue, rowNumber);
        if (!validation.isValid) {
          errorRow.push(`Dòng ${rowNumber}: \n ${validation.errors}`);
          rowValid = false;
        }
        data[mapping.fieldName] = cellValue;
      })
      if (hasValue) {
        errors.push(...errorRow);
        data['success'] = rowValid ? null : false;
        jsonData.push(data);
      }
    });
    return {
      sheetName: sheet.name,
      data: jsonData,
      errors: errors.length > 0 ? errors : undefined
    };
  }
  async parseExcel(arrayBuffer: any): Promise<ParseResult> {

    const headerMappingMap = new Map<string, HeaderMapping>(
      this.headerMappings.map(header => [header.fieldName, header])
    );
    // Lọc và kết hợp các thông tin cần thiết từ ValidationConfig và HeaderMapping
    const filteredConfig: ValidationConfig = Object.entries(this.validationConfig)
      .filter(([fieldName, validator]) => headerMappingMap.has(fieldName))  // Kiểm tra sự tồn tại trong headerMappingMap
      .reduce((acc, [fieldName, validator]) => {
        // Thêm thông tin từ headerMapping vào mỗi validator
        const headerMapping = headerMappingMap.get(fieldName);
        if (headerMapping) {
          acc[fieldName] = {
            ...validator,
            required: headerMapping.required[this.parseOptions.typeImport],
            uniqueCheck: {
              enabled: headerMapping.unique[this.parseOptions.typeImport],
              errorMessage: headerMapping.excelHeader + ' bị trùng lặp',
            } // Thêm headerMapping vào validator
          };
        }
        return acc;
      }, {} as ValidationConfig);
    this.validationService = new ValidationService(filteredConfig);

    const workbook = new ExcelJS.Workbook()
    try {
      await workbook.xlsx.load(arrayBuffer);
      const result: ParseResult = {
        success: true,
        sheets: [],
        listData: [],
        globalErrors: [],
      };

      let sheetsToProcess: ExcelJS.Worksheet[];
      if (this.parseOptions.sheets && this.parseOptions.sheets.length > 0) {
        sheetsToProcess = this.parseOptions.sheets.map(
          index => workbook.getWorksheet(index)
        ).filter((sheet): sheet is ExcelJS.Worksheet => sheet !== undefined);
      } else {
        sheetsToProcess = workbook.worksheets;
      }              // Kiểm tra nếu không có sheet nào được xử lý
      if (sheetsToProcess.length === 0) {
        return {
          success: false,
          sheets: [],
          listData: [],
          globalErrors: ['Không tìm thấy sheet nào để xử lý']
        };
      }
      sheetsToProcess.forEach(sheet => {
        const sheetData = this.parseSheet(sheet);
        result.sheets.push(sheetData);
        if (sheetData.errors) {
          result.success = false;
        }
      });
      result.listData = result.sheets.reduce((acc, sheet) => [...acc, ...sheet.data], [] as any[]);
      if(result.listData.length<=0){
        if(result.success){
          result.success = false;
          result.globalErrors = ['Không có dữ liệu']
        }else{
          result.globalErrors = ['Sai mẫu import Excel']
        }
      }
      return result;

    } catch (error) {
      return {
        success: false,
        sheets: [],
        globalErrors: [`Sai định dạng file Excel!`],
        listData: [],
      };
    }
  }
}

