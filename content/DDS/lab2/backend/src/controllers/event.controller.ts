import { Request, Response } from 'express';
import * as eventService from '../services/event.service';

export async function createEvent(req: Request, res: Response) {
  if (!req.user) return res.status(401).json({ error: 'Unauthorized' });

  try {
    const event = await eventService.createEvent(req.user.userId, req.body);
    res.status(201).json(event);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to create event' });
  }
}

export async function updateEvent(req: Request, res: Response) {
  if (!req.user) return res.status(401).json({ error: 'Unauthorized' });

  try {
    const event = await eventService.updateEvent(req.params.eventId, req.user.userId, req.body);
    res.json(event);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to update event' });
  }
}

export async function getEvents(req: Request, res: Response) {
  try {
    const { page, limit, tags, searchQuery, sortOrder } = req.query;

    const events = await eventService.getEvents(
      Number(page) || 1,
      Number(limit) || 10,
      tags ? (tags as string).split(',') : undefined,
      searchQuery as string,
      sortOrder as 'asc' | 'desc'
    );

    res.json(events);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to fetch events' });
  }
}

export async function getEventById(req: Request, res: Response) {
  try {
    const event = await eventService.getEventById(req.params.eventId);
    res.json(event);
  } catch (error) {
    res.status(404).json({ error: error instanceof Error ? error.message : 'Event not found' });
  }
}

export async function subscribeToEvent(req: Request, res: Response) {
  if (!req.user) return res.status(401).json({ error: 'Unauthorized' });

  try {
    const event = await eventService.subscribeToEvent(req.params.eventId, req.user.userId);
    res.json(event);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to subscribe' });
  }
}

export async function unsubscribeFromEvent(req: Request, res: Response) {
  if (!req.user) return res.status(401).json({ error: 'Unauthorized' });

  try {
    const event = await eventService.unsubscribeFromEvent(req.params.eventId, req.user.userId);
    res.json(event);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to unsubscribe' });
  }
}

export async function getUserEvents(req: Request, res: Response) {
  if (!req.user) return res.status(401).json({ error: 'Unauthorized' });

  try {
    const events = await eventService.getUserEvents(req.user.userId);
    res.json(events);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to fetch user events' });
  }
}

export async function removeParticipant(req: Request, res: Response) {
  if (!req.user) return res.status(401).json({ error: 'Unauthorized' });

  try {
    const { eventId, userId } = req.params;
    const event = await eventService.removeParticipant(eventId, req.user.userId, userId);
    res.json(event);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to remove participant' });
  }
}

export async function getEventParticipants(req: Request, res: Response) {
  if (!req.user) return res.status(401).json({ error: 'Unauthorized' });

  try {
    const participants = await eventService.getEventParticipants(req.params.eventId, req.user.userId);
    res.json(participants);
  } catch (error) {
    res.status(400).json({ error: error instanceof Error ? error.message : 'Failed to fetch participants' });
  }
}
