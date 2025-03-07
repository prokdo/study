import { Role } from '../enums/role.enum';
import { Sex } from '../enums/sex.enum';

export interface User {
    _id: string;
    email: string;
    passwordHash: string;
    role: Role;
    username: string;
    surname?: string;
    firstName?: string;
    lastName?: string;
    birthDate?: Date;
    sex?: Sex;
    createdAt: Date;
}
