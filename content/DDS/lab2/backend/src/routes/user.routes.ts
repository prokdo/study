import { NextFunction, Router, Request, Response } from 'express';
import { getProfile, updateProfile, changePassword, getUserById } from '../controllers/user.controller';
import { verifyToken } from '../middlewares/auth.middleware';

const router = Router();

router.use((req: Request, res: Response, next: NextFunction) => {
    verifyToken(req, res, next);
});

router.get('/profile', getProfile);
router.put('/profile', updateProfile);
router.put('/profile/password', changePassword);
router.get('/:userId', getUserById);

export default router;