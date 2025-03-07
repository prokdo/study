import { Request } from 'express';
import { Role } from '../enums/role.enum';

export interface AuthRequest extends Request {
    user?: {
      userId: string;
      role: Role;
    };
}