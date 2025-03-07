import { UserModel } from '../models/user.model';
import { RefreshTokenModel } from '../models/refresh-token.model';
import { generateAccessToken, generateRefreshToken, getExpirationDate, hashPassword, verifyPassword } from '../utils/helpers';
import bcrypt from 'bcryptjs';
import { Role } from '../enums/role.enum';
import { env } from '../config/env';

export async function register(email: string, password: string, role: Role) {
  if (await UserModel.exists({ email })) throw new Error('User already exists');

  const passwordHash = await hashPassword(password);
  const user = await UserModel.create({ email, passwordHash, role });

  const accessToken = generateAccessToken({ userId: user._id.toString(), role: role });
  const refreshToken = await createRefreshToken(user._id.toString(), role);

  return { accessToken, refreshToken };
}

export async function login(email: string, password: string) {
  const user = await UserModel.findOne({ email });
  if (!user || !(await verifyPassword(password, user.passwordHash))) throw new Error('Invalid credentials');

  const accessToken = generateAccessToken({ userId: user._id.toString(), role: user.role });
  const refreshToken = await createRefreshToken(user._id.toString(), user.role);

  return { accessToken, refreshToken };
}

export async function createRefreshToken(userId: string, role: Role) {
  const token = generateRefreshToken({ userId, role });
  const tokenHash = await bcrypt.hash(token, env.BCRYPT_SALT_ROUNDS);

  await RefreshTokenModel.updateOne(
    { userId },
    { tokenHash, expiresAt: getExpirationDate(), isRevoked: false },
    { upsert: true }
  );

  return token;
}

export async function refreshToken(oldRefreshToken: string) {
  const tokenDoc = await RefreshTokenModel.findOne({ isRevoked: false }).lean();
  if (!tokenDoc || !(await bcrypt.compare(oldRefreshToken, tokenDoc.tokenHash))) {
    throw new Error('Invalid refresh token');
  }

  const user = await UserModel.findById(tokenDoc.userId).lean();
  if (!user) throw new Error('User not found');

  const accessToken = generateAccessToken({ userId: user._id.toString(), role: user.role });
  const newRefreshToken = await createRefreshToken(user._id.toString(), user.role);

  return { accessToken, refreshToken: newRefreshToken };
}
