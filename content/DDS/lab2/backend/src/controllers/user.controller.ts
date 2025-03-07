import { Request, Response } from 'express';
import { updateUser, changeUserPassword, getUserById as getUserByIdService } from '../services/user.service';
import { UpdateUserDto } from '../dtos/user.dto';

export async function getProfile(req: Request, res: Response) {
  try {
    const userId = req.user!.userId;
    const user = await getUserByIdService(userId);
    res.json(user);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to fetch profile' });
  }
}

export async function updateProfile(req: Request, res: Response) {
  try {
    const userId = req.user!.userId;
    const dto: UpdateUserDto = req.body;
    const updatedUser = await updateUser(userId, dto);
    res.json(updatedUser);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to update profile' });
  }
}

export async function changePassword(req: Request, res: Response) {
  try {
    const userId = req.user!.userId;
    const { oldPassword, newPassword } = req.body;
    const updatedUser = await changeUserPassword(userId, oldPassword, newPassword);
    res.json(updatedUser);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to change password' });
  }
}

export async function getUserById(req: Request, res: Response) {
  try {
    const userId = req.params.userId;
    const user = await getUserByIdService(userId);
    res.json(user);
  } catch (error) {
    res.status(404).json({ error: error instanceof Error ? error.message : 'User not found' });
  }
}
