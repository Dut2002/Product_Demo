import { PermissionRequest } from "./permission-request";

export interface ChangeRolePermission{
  roleId: number;
  permissions: PermissionRequest[];
}
