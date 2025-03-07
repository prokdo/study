import mongoose, { Schema, Document } from 'mongoose';
import { Event } from '../interfaces/event.interface';

export interface IEvent extends Omit<Event, '_id'>, Document {
  _id: string;
}

const eventSchema = new Schema<IEvent>(
  {
    name: { type: String, required: true, minLength: 3 },
    description: { type: String, required: true, minLength: 10 },
    date: { type: Date, required: true },
    location: { type: String, required: true },
    capacity: { type: Number, min: 1 },
    participantsId: { type: [Schema.Types.ObjectId], ref: 'User', default: [] },
    tags: { type: [String], default: [] },
    authorId: { type: Schema.Types.ObjectId, ref: 'User', required: true },
  },
  { timestamps: true }
);

export const EventModel = mongoose.model<IEvent>('Event', eventSchema);
