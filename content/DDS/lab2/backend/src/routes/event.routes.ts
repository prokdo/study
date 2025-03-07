import { Router } from 'express';
import {
  createEvent,
  updateEvent,
  getEvents,
  getEventById,
  subscribeToEvent,
  unsubscribeFromEvent,
  getUserEvents,
  removeParticipant,
  getEventParticipants,
} from '../controllers/event.controller';
import { verifyToken } from '../middlewares/auth.middleware';

const router = Router();

router.get('/', getEvents);
router.get('/:eventId', getEventById);

router.use(verifyToken);

router.post('/', createEvent);
router.put('/:eventId', updateEvent);
router.post('/:eventId/subscribe', subscribeToEvent);
router.post('/:eventId/unsubscribe', unsubscribeFromEvent);
router.get('/my-events', getUserEvents);
router.get('/:eventId/participants', getEventParticipants);
router.delete('/:eventId/participants/:userId', removeParticipant);

export default router;
