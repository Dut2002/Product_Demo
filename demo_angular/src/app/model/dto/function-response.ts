import { Permission } from "../permission";

export interface FunctionResponse{
  id: number;
  name: string;
  permissions: Permission[]
}
