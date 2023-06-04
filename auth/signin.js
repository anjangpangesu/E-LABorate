const express = require('express');
const auth = require('./auth');
const Joi = require('joi');
const admin = require('firebase-admin');
const userLog = require('../log/logger');

const router = express.Router();
const db = admin.firestore();

// Validate user input using Joi
const validateUserInput = (data) => {
  const schema = Joi.object({
    email: Joi.string()
      .email({ minDomainSegments: 2, tlds: { allow: ['com', 'net'] } })
      .trim()
      .required(),
    password: Joi.string().required()
  });

  return schema.validate(data);
};

// Route to Sign In
router.post('/', async (req, res) => {
  const { email, password } = req.body;

  // Validasi input pengguna
  const { error } = validateUserInput(req.body);
  if (error) {
    return res.status(400).json({
      error: error.details[0].message
    });
  }

  // Retrieve user data from Firestore
  try {
    const userDoc = await db.collection('users').where('email', '==', email).get();
    if (userDoc.empty) {
      return res.status(401).json({
        error: error,
        message: "Invalid email or password"
      });
    }

    const userData = userDoc.docs[0].data();
    const userId = userData.userId;
    const username = userData.username;

    // Verify passwords using bcrypt
    const passwordMatch = await auth.verifyPassword(password, userData.password);
    if (!passwordMatch) {
      return res.status(401).json({
        error: error,
        message: "Invalid email or password"
      });
    }

    // Create JWT tokens
    const token = auth.generateToken(userId);

    // Save the token into the user's collection
    await db.collection('users').doc(userId).update({
      token: token
    });

    // Logging log messages to userLog
    userLog.info('SIGNED IN', { userId, username, email, phone: userData.phone, address: userData.address });

    return res.status(200).json({
      error: false,
      message: "User Signed In",
      userId: userId,
      email: email,
      username: username,
      phone: userData.phone,
      address: userData.address,
      token
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: "Error Signing In"
    });
  }
});

module.exports = router;
