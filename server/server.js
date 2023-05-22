const express = require('express');
const app = express();
// const { myLogger } = require('./../log/logger');
const signupRouter = require('./../routes/signup');
const signinRouter = require('./../routes/signin');
const resetRouter = require('./../routes/reset');

// Middleware untuk mengizinkan body request dalam format JSON
app.use(express.json());

// Routes
// app.use(myLogger);
app.use('/signup', signupRouter);
app.use('/signin', signinRouter);
app.use('/resetpassword', resetRouter);

// Mulai server
app.listen(8080, () => {
  console.log('Server started on port http://localhost:8080');
});