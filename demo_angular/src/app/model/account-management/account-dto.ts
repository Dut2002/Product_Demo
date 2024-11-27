import { RoleRes } from "../dto/role-res";
import { RoleDto } from "./role-dto";

export interface AccountDto{
  id: number;
  fullName: string;
  email: string;
  status: AccountDto;
  roles: RoleDto[];
}
