import { Role } from "../enums/role.enum";

export interface JwtPayload {
    userId: string;
    role: Role;
}