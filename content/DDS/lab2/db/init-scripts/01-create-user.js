const dbName    = process.env.MONGO_INITDB_DATABASE;
const user      = process.env.MONGO_APP_USER;
const pwd       = process.env.MONGO_APP_PASSWORD;

db.createUser({
    user: user,
    pwd: pwd,
    roles: [{ role: "readWrite", db: dbName }]
})