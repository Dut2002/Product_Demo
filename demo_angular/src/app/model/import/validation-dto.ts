// interfaces.ts
export interface ValidationRule {
  type: string;
  message?: string;
  params?: any;
}

export interface ValidationResult {
  isValid: boolean;
  errors: string[];
}

export interface FieldValidator {
  required?: boolean;
  rules: ValidationRule[];
  uniqueCheck?: {
    enabled: boolean;
    errorMessage?: string;
  };
}

export interface ValidationConfig {
  [fieldName: string]: FieldValidator;
}
