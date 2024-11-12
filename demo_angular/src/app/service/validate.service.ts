import { ValidationConfig, ValidationResult } from "../model/import/validation-dto";

export class ValidationService {
  private static readonly BUILT_IN_VALIDATORS = {
    required: (value: any, param?: any, message?: string): string | null => {
      // Kiểm tra nếu giá trị là null, undefined, hoặc chuỗi trống
      if (value === null || value === undefined || value === '') {
        return message || 'Trường này không được để trống';
      }

      // Nếu value là chuỗi và có thể gọi trim
      if (typeof value === 'string' && value.trim() === '') {
        return message || 'Trường này không được để trống';
      }
      return null;
    },

    maxLength: (value: any, params?: number, message?: string): string | null => {
      if (value && value.toString().length > params!) {
        return message || `Độ dài không được vượt quá ${params} ký tự`;
      }
      return null;
    },

    minLength: (value: any, params?: number, message?: string): string | null => {
      if (value && value.toString().length < params!) {
        return message || `Độ dài tối thiểu là ${params} ký tự`;
      }
      return null;
    },

    pattern: (value: any, params?: RegExp, message?: string): string | null => {
      if (value && !params!.test(value.toString())) {
        return message || 'Giá trị không đúng định dạng';
      }
      return null;
    },

    email: (value: any, params?: any, message?: string): string | null => {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (value && !emailPattern.test(value.toString())) {
        return message || 'Email không đúng định dạng';
      }
      return null;
    },

    numeric: (value: any, params?: any, message?: string): string | null => {
      if (value && (typeof value !== 'number' || isNaN(value))) {
        return message || 'Giá trị phải là số';
      }
      return null;
    },

    minValue: (value: any, params?: number, message?: string): string | null => {
      if (value === null || value === undefined) return null;

      const numValue = Number(value);
      if (isNaN(numValue)) {
        return 'Giá trị không phải là số hợp lệ';
      }

      if (numValue < params!) {
        return message || `Giá trị phải lớn hơn hoặc bằng ${params}`;
      }
      return null;
    },

    maxValue: (value: any, params?: number, message?: string): string | null => {
      if (value === null || value === undefined) return null;

      const numValue = Number(value);
      if (isNaN(numValue)) {
        return 'Giá trị không phải là số hợp lệ';
      }

      if (numValue > params!) {
        return message || `Giá trị phải nhỏ hơn hoặc bằng ${params}`;
      }
      return null;
    },

    range: (value: any, params?: { min: number; max: number }, message?: string): string | null => {
      if (value === null || value === undefined) return null;

      const numValue = Number(value);
      if (isNaN(numValue)) {
        return 'Giá trị không phải là số hợp lệ';
      }

      if (numValue < params!.min || numValue > params!.max) {
        return message || `Giá trị phải nằm trong khoảng từ ${params!.min} đến ${params!.max}`;
      }
      return null;
    },

    date: (value: any, params?: { format?: string }, message?: string): string | null => {
      if (!value) return null;
      if (value instanceof Date ||
        (typeof value === 'number' && !isNaN(value) && value > 0)) {
        return null;
      }
      else {
        return message || 'Ngày tháng không hợp lệ';
      }
    },

    minDate: (value: any, params?: Date | string, message?: string): string | null => {
      if (!value) return null;

      const date = new Date(value);
      const minDate = new Date(params!);

      if (isNaN(date.getTime()) || isNaN(minDate.getTime())) {
        return 'Ngày tháng không hợp lệ';
      }

      if (date < minDate) {
        return message || `Ngày phải sau ${minDate.toLocaleDateString('vi-VN')}`;
      }
      return null;
    },

    maxDate: (value: any, params?: Date | string, message?: string): string | null => {
      if (!value) return null;

      const date = new Date(value);
      const maxDate = new Date(params!);

      if (isNaN(date.getTime()) || isNaN(maxDate.getTime())) {
        return 'Ngày tháng không hợp lệ';
      }

      if (date > maxDate) {
        return message || `Ngày phải trước ${maxDate.toLocaleDateString('vi-VN')}`;
      }
      return null;
    },

    dateRange: (value: any, params?: { min: Date | string; max: Date | string }, message?: string): string | null => {
      if (!value) return null;

      const date = new Date(value);
      const minDate = new Date(params!.min);
      const maxDate = new Date(params!.max);

      if (isNaN(date.getTime()) || isNaN(minDate.getTime()) || isNaN(maxDate.getTime())) {
        return 'Ngày tháng không hợp lệ';
      }

      if (date < minDate || date > maxDate) {
        return message || `Ngày phải nằm trong khoảng từ ${minDate.toLocaleDateString('vi-VN')} đến ${maxDate.toLocaleDateString('vi-VN')}`;
      }
      return null;
    }

  }

  private uniqueValues: Map<string, Set<string>> = new Map();

  constructor(private config: ValidationConfig) {
    // Initialize unique value tracking for fields with uniqueCheck enabled
    Object.entries(config).forEach(([fieldName, validator]) => {
      if (validator.uniqueCheck?.enabled) {
        this.uniqueValues.set(fieldName, new Set());
      }
    });
  }

  validateField(fieldName: string, value: any, rowNumber: number): ValidationResult {
    const validator = this.config[fieldName];
    if (!validator) {
      return { isValid: true, errors: [] };
    }

    const errors: string[] = [];

    // Required check
    if (validator.required) {
      const error = ValidationService.BUILT_IN_VALIDATORS.required(value,null, fieldName + ' không được để trống');
      if (error) errors.push(error);
    }

    // Apply all rules
    for (const rule of validator.rules) {
      const validatorFn = ValidationService.BUILT_IN_VALIDATORS[rule.type as keyof typeof ValidationService.BUILT_IN_VALIDATORS];
      if (validatorFn) {
        const error = validatorFn(value, rule.params, rule.message);
        if (error) errors.push(error);
      }
    }

    // Unique check
    if (validator.uniqueCheck && validator.uniqueCheck?.enabled && value !== null && value !== undefined) {
      const uniqueSet = this.uniqueValues.get(fieldName)!;
      const valueStr = value.toString();
      if (uniqueSet.has(valueStr)) {
        errors.push(validator.uniqueCheck.errorMessage ||
          `Giá trị '${value}' tại dòng '${rowNumber}' của cột '${fieldName}' đã bị trùng lặp`);
      } else {
        uniqueSet.add(valueStr);
      }
    }

    return {
      isValid: errors.length === 0,
      errors
    };
  }

  clearUniqueChecks() {
    this.uniqueValues.forEach(set => set.clear());
  }
}
