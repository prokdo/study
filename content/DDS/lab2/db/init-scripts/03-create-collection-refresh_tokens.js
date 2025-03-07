db.createCollection("refresh_tokens", {
    validator: {
        $jsonSchema: {
            bsonType:   "object",
            required:   [ "user_id", "token_hash", "expires_at" ],
            properties: {
                user_id:    { bsonType: "objectId" },
                token_hash: { bsonType: "string", minLength: 64 },
                expires_at: { bsonType: "date" },
                created_at: { bsonType: "date" },
                is_revoked: { bsonType: "bool" }
            }
        }
    }
});

db.refresh_tokens.createIndex(
    { expires_at: 1 },
    { expireAfterSeconds: 0 }
);

db.refresh_tokens.createIndex(
    { token_hash: 1 },
    { unique: true }
);

db.refresh_tokens.createIndex({ expires_at: 1, is_revoked: 1 });