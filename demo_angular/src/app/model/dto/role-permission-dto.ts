import { FunctionDto } from "./function-dto";

export interface RolePermissionDto{
  id: number;
  name: string;
  functions: FunctionDto[]
}
