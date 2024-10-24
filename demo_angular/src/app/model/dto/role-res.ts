import { FunctionDto } from "./function-dto";

export interface RoleRes{
  id: number;
  name: string;
  functions: FunctionDto[];
}
