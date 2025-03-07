import bcrypt from 'bcryptjs';
import jwt, { SignOptions, Algorithm } from 'jsonwebtoken';
import { Request, Response, NextFunction } from 'express';
import { env } from '../config/env';
import { JwtPayload } from '../interfaces/jwt-payload.interface';

export function hashPassword(password: string): Promise<string> {
  return bcrypt.hash(password, env.BCRYPT_SALT_ROUNDS);
}

export function verifyPassword(password: string, hashedPassword: string): Promise<boolean> {
  return bcrypt.compare(password, hashedPassword);
}

export function generateJwtToken(payload: JwtPayload, secret: string, expiresIn: string | number): string {
  const options: SignOptions = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    expiresIn: expiresIn as any,
    algorithm: env.JWT_HASH_ALGORITHM as Algorithm
  };
  return jwt.sign(payload, secret, options);
}

export function generateAccessToken(payload: JwtPayload): string {
  return generateJwtToken(payload, env.JWT_SECRET, env.JWT_ACCESS_TOKEN_EXPIRES);
}

export function generateRefreshToken(payload: JwtPayload): string {
  return generateJwtToken(payload, env.JWT_REFRESH_SECRET, env.JWT_REFRESH_TOKEN_EXPIRES);
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export function errorHandler(err: Error, _0: Request, res: Response, _1: NextFunction) {
  console.error(err.stack);
  res.status(500).json({ error: 'Internal Server Error' });
}

export function checkDocumentExist<T>(document: T | null, errorMessage: string) {
  if (!document) {
    throw new Error(errorMessage);
  }
}

export function getExpirationDate(): Date {
  const expiresIn = process.env.JWT_REFRESH_TOKEN_EXPIRES || '7d';
  const unit = expiresIn.slice(-1);
  const value = parseInt(expiresIn.slice(0, -1), 10);

  const unitMap: Record<string, number> = { s: 1, m: 60, h: 3600, d: 86400 };
  const seconds = value * (unitMap[unit] || 1);

  return new Date(Date.now() + seconds * 1000);
}