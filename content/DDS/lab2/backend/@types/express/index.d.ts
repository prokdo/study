import { Role } from '../../src/enums/role.enum';

declare module 'express' {
  interface Request {
    user?: {
      userId: string;
      role: Role;
    };
  }
}