db.createCollection("users", {
    validator: {
        $jsonSchema: {
            bsonType:   "object",
            required:   [ "email", "password_hash", "role", "username" ] ,
            properties: {
                email:          { bsonType: "string" },
                password_hash:  { bsonType: "string", minLength: 60 },
                role:           { enum: [ "USER", "MODERATOR" ] },
                username:       { bsonType: "string" },
                surname:        { bsonType: "string" },
                first_name:     { bsonType: "string" },
                last_name:      { bsonType: "string" },
                birth_date:     { bsonType: "date" },
                sex:            { enum: ["MALE", "FEMALE", "NOT_STATED"] },
                created_at:     { bsonType: "date" }
            }
        }
    }
});

db.users.createIndex({ email: 1 }, { unique: true });
db.users.createIndex({ username: 1 }, { unique: true });
db.users.createIndex({ role: 1 });

db.users.insertOne({
    email:          process.env.MONGO_INIT_MODERATOR_EMAIL,
    password_hash:  process.env.MONGO_INIT_MODERATOR_PASSWORD_HASH,
    role:           "MODERATOR",
    username:       "moderator_1",
    created_at:     new Date()
})