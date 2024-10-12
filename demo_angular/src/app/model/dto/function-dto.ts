import { RoleAccess } from "./role-access"

export interface FunctionDto{
  id: number
  name: string
  endPoint: string
  roleAccesses: RoleAccess[]
}
