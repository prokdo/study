import dotenv from 'dotenv';
import path from 'path';

dotenv.config({
    path: path.resolve(__dirname, '../../.env')
});

const requiredEnvVars = [
    'MONGO_URI',
    'JWT_SECRET',
    'JWT_REFRESH_SECRET',
    'PORT',
    'BCRYPT_SALT_ROUNDS',
    'JWT_ACCESS_TOKEN_EXPIRES',
    'JWT_REFRESH_TOKEN_EXPIRES',
    'JWT_HASH_ALGORITHM'
];
requiredEnvVars.forEach((key) => {
    if (!process.env[key]) {
        throw new Error(`Missing environment variable: ${key}`);
  }
});

export const env = {
    PORT: parseInt(process.env.PORT || '3030', 10),
    MONGO_URI: process.env.MONGO_URI!,
    JWT_SECRET: process.env.JWT_SECRET || 'DEV',
    JWT_REFRESH_SECRET: process.env.JWT_REFRESH_SECRET || 'DEV_REFRESH',
    BCRYPT_SALT_ROUNDS: parseInt(process.env.BCRYPT_SALT_ROUNDS || '12', 10),
    JWT_ACCESS_TOKEN_EXPIRES: process.env.JWT_ACCESS_TOKEN_EXPIRES || '15m',
    JWT_REFRESH_TOKEN_EXPIRES: process.env.JWT_REFRESH_TOKEN_EXPIRES || '7d',
    JWT_HASH_ALGORITHM: process.env.JWT_HASH_ALGORITHM || 'HS256'
};