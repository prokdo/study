import { EventModel } from '../models/event.model';
import { UserModel } from '../models/user.model';
import { checkDocumentExist } from '../utils/helpers';
import { CreateEventDto, UpdateEventDto } from '../dtos/event.dto';

export async function createEvent(userId: string, dto: CreateEventDto) {
  const event = await EventModel.create({ ...dto, authorId: userId, participantsId: [] });
  return event;
}

export async function updateEvent(eventId: string, userId: string, dto: UpdateEventDto) {
  const event = await EventModel.findById(eventId);
  checkDocumentExist(event, 'Event not found');

  if (event!.authorId.toString() !== userId) {
    throw new Error('Permission denied');
  }

  Object.entries(dto).forEach(([key, value]) => {
    if (value !== undefined) {
      event!.set(key, value);
    }
  });
  await event!.save();

  return event;
}

export async function getEvents(
  page = 1,
  limit = 10,
  tags?: string[],
  searchQuery?: string,
  sortOrder: 'asc' | 'desc' = 'asc'
) {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const filter: any = {};

  if (tags?.length) {
    filter.tags = { $in: tags };
  }

  if (searchQuery) {
    filter.name = { $regex: searchQuery, $options: 'i' };
  }

  const sortOption = sortOrder === 'asc' ? 1 : -1;

  const events = await EventModel.find(filter)
    .skip((page - 1) * limit)
    .limit(limit)
    .sort({ date: sortOption });

  return events;
}

export async function getEventById(eventId: string) {
  const event = await EventModel.findById(eventId);
  checkDocumentExist(event, 'Event not found');
  return event;
}

export async function subscribeToEvent(eventId: string, userId: string) {
  const event = await EventModel.findById(eventId);
  checkDocumentExist(event, 'Event not found');

  if (event!.participantsId.includes(userId)) {
    throw new Error('Already subscribed');
  }

  if (event!.capacity && event!.participantsId.length >= event!.capacity) {
    throw new Error('Event is full');
  }

  event!.participantsId.push(userId);
  await event!.save();

  return event;
}

export async function unsubscribeFromEvent(eventId: string, userId: string) {
  const event = await EventModel.findById(eventId);
  checkDocumentExist(event, 'Event not found');

  event!.participantsId = event!.participantsId.filter(id => id.toString() !== userId);
  await event!.save();

  return event;
}

export async function getUserEvents(userId: string) {
  return await EventModel.find({ authorId: userId });
}

export async function getEventParticipants(eventId: string, userId: string) {
  const event = await EventModel.findById(eventId);
  checkDocumentExist(event, 'Event not found');

  if (event!.authorId.toString() !== userId) {
    throw new Error('Permission denied');
  }

  const participants = await UserModel.find({ _id: { $in: event!.participantsId } }).select('-passwordHash');
  return participants;
}

export async function removeParticipant(eventId: string, userId: string, targetUserId: string) {
  const event = await EventModel.findById(eventId);
  checkDocumentExist(event, 'Event not found');

  if (event!.authorId.toString() !== userId) {
    throw new Error('Permission denied');
  }

  event!.participantsId = event!.participantsId.filter(id => id.toString() !== targetUserId);
  await event!.save();

  return event;
}