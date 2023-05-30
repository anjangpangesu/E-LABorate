const express = require('express');
const app = express();
const userRouter = require('./../auth/user');
const signupRouter = require('./../auth/signup');
const signinRouter = require('./../auth/signin');
const signoutRouter = require('./../auth/signout');
const forgotpassRouter = require('../auth/forgotpass');
const verifycodeRouter = require('./../auth/verifycode');
const resetpassRouter = require('./../auth/resetpass');

// Middleware to allow request body in JSON format
app.use(express.json());

// Routes
app.use('/', userRouter)
app.use('/signup', signupRouter);
app.use('/signin', signinRouter);
app.use('/signout', signoutRouter);
app.use('/forgot-password', forgotpassRouter);
app.use('/verify-code', verifycodeRouter);
app.use('/reset-password', resetpassRouter);

// Server startup
app.listen(8080, () => {
  console.log('Server started on port http://localhost:8080');
});
