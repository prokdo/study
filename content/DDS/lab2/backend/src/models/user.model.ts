import mongoose, { Schema, Document } from 'mongoose';
import { User } from '../interfaces/user.interface';
import { Role } from '../enums/role.enum';
import { Sex } from '../enums/sex.enum';

export interface IUser extends Omit<User, '_id'>, Document {
  _id: string;
}

const userSchema = new Schema<IUser>(
  {
    email: { type: String, required: true, unique: true },
    passwordHash: { type: String, required: true, minLength: 60 },
    role: { type: String, enum: Object.values(Role), default: Role.USER },
    username: { type: String, required: true, unique: true },
    surname: { type: String, required: false },
    firstName: { type: String, required: false },
    lastName: { type: String, required: false },
    birthDate: { type: Date, required: false },
    sex: { type: String, enum: Object.values(Sex), default: Sex.NOT_STATED },
  },
  { timestamps: true }
);

export const UserModel = mongoose.model<IUser>('User', userSchema);
