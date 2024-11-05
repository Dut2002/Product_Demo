import { PermissionDto } from "./permission-dto";

export interface FunctionDto{
  id: number;
  name: string;
  feRoute: string;
  permissions: PermissionDto[];
}
