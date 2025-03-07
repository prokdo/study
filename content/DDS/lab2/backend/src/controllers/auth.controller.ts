import { Request, Response, NextFunction } from 'express';
import * as AuthService from '../services/auth.service';
import { Role } from '../enums/role.enum';

export async function register(req: Request, res: Response, next: NextFunction) {
  try {
    const { email, password, role } = req.body;
    const userRole = Object.values(Role).includes(role) ? role : Role.USER;
    const tokens = await AuthService.register(email, password, userRole);
    res.status(201).json(tokens);
  } catch (error) {
    next(error);
  }
}

export async function login(req: Request, res: Response, next: NextFunction) {
  try {
    const { email, password } = req.body;
    const tokens = await AuthService.login(email, password);
    res.json(tokens);
  } catch (error) {
    next(error);
  }
}

export async function refreshToken(req: Request, res: Response, next: NextFunction) {
  try {
    const { refreshToken } = req.body;
    const tokens = await AuthService.refreshToken(refreshToken);
    res.json(tokens);
  } catch (error) {
    next(error);
  }
}
