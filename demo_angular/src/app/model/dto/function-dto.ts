import { PermissionDto } from "./permission-dto";

export interface FunctionDto{
  id: number;
  name: string;
  permissions: PermissionDto[];
}
