import { UpdateUserDto } from '../dtos/user.dto';
import { UserModel } from '../models/user.model';
import { checkDocumentExist, hashPassword, verifyPassword } from '../utils/helpers';

export async function getUserById(userId: string) {
  const user = await UserModel.findById(userId).select('-passwordHash');
  checkDocumentExist(user, 'User not found');
  return user;
}

export async function updateUser(userId: string, dto: UpdateUserDto) {
    const user = await UserModel.findById(userId);
    checkDocumentExist(user, 'User not found');

    Object.entries(dto).forEach(([key, value]) => {
      if (value !== undefined) {
        user!.set(key, value);
      }
    });

    await user!.save();
    return user;
  }

export async function changeUserPassword(userId: string, oldPassword: string, newPassword: string) {
  const user = await UserModel.findById(userId);
  checkDocumentExist(user, 'User not found');

  if (!(await verifyPassword(oldPassword, user!.passwordHash))) {
    throw new Error('Incorrect old password');
  }

  user!.passwordHash = await hashPassword(newPassword);
  await user!.save();
  return user;
}

export async function deleteUser(userId: string) {
  const user = await UserModel.findByIdAndDelete(userId);
  checkDocumentExist(user, 'User not found');
}
