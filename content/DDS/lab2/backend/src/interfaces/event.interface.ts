import { Ref } from '@typegoose/typegoose';

import { User } from './user.interface';

export interface Event {
    _id: string;
    authorId: Ref<User>;
    name: string;
    description: string;
    date: Date;
    location: string;
    capacity?: number;
    participantsId: Ref<User>[];
    tags: string[];
    createdAt: Date;
}