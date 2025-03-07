db.createCollection("events", {
    validator: {
            $jsonSchema: {
            bsonType:   "object",
            required:   [ "author_id", "name", "description", "date", "location" ],
            properties: {
                author_id:        { bsonType: "objectId" },
                name:             { bsonType: "string" },
                description:      { bsonType: "string" },
                date:             { bsonType: "date" },
                location:         { bsonType: "string" },
                capacity:         { bsonType: "int", minimum: 1 },
                participants_id:  {
                                    bsonType: "array",
                                    items: { bsonType: "objectId" },
                                    uniqueItems: true
                                  },
                created_at:       { bsonType: "date" },
                tags:             {
                                    bsonType: "array",
                                    items: { bsonType: "string", minLength: 1 },
                                    uniqueItems: true
                                  }
            }
        }
    }
});

db.events.createIndex({ author_id: 1 });
db.events.createIndex({ date: 1 });
db.events.createIndex({ tags: "text" });
db.events.createIndex({ participants_id: 1 });
db.events.createIndex({ name: 1 });