import { Ref } from "@typegoose/typegoose";

import { User } from "./user.interface";

export interface RefreshToken {
    _id: string;
    userId: Ref<User>;
    tokenHash: string;
    expiresAt: Date;
    createdAt: Date;
    isRevoked: boolean;
}