const express = require('express');
const app = express();
const userRouter = require('./../auth/user');
const signupRouter = require('./../auth/signup');
const signinRouter = require('./../auth/signin');
const signoutRouter = require('./../auth/signout');
const forgotpassRouter = require('../auth/forgotpass');
const verifycodeRouter = require('./../auth/verifycode');
const resetpassRouter = require('./../auth/resetpass');
const doctorRouter = require('../doctor/doctors');
const addDoctorRouter = require('./../doctor/addDoctor');
const workoutRouter = require('../workout/workouts');
const addWorkoutRouter = require('./../workout/addWorkout');
const medicineRouter = require('../medicine/medicine');
const addMedicineRouter = require('../medicine/addMedicine');
const cartRouter = require('../checkout/cart');
const addToCartRouter = require('../checkout/addToCart');

// Middleware to allow request body in JSON format
app.use(express.json());

// Public Routes
app.use('/', userRouter, doctorRouter, workoutRouter, medicineRouter, addToCartRouter, cartRouter)
app.use('/signup', signupRouter);
app.use('/signin', signinRouter);
app.use('/signout', signoutRouter);
app.use('/forgot-password', forgotpassRouter);
app.use('/verify-code', verifycodeRouter);
app.use('/reset-password', resetpassRouter);

// Private Routes
app.use('/private', addDoctorRouter, addWorkoutRouter, addMedicineRouter);

// Handle root URL
app.get('/', (req, res) => {
  res.redirect('/signin');
});

// Server startup
app.listen(8080, () => {
  console.log('Server started on port http://localhost:8080');
});
