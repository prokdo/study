import express, { Application, Request, Response } from 'express';

import { env } from './config/env';
import { connectDB } from './config/db';

const app: Application = express();

connectDB();

app.get('/', (_: Request, res: Response) => {
  res.send('Hello from Express + TypeScript!');
});

app.listen(env.PORT, () => {
  console.log(`App is running on http://localhost:${env.PORT}`);
});