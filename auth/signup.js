const express = require('express');
const Joi = require('joi');
const bcrypt = require('bcrypt');
const admin = require('firebase-admin');
const userLog = require('./../log/logger');
const router = express.Router();
const db = admin.firestore();

// Validate user input using Joi
const validateUserInput = (data) => {
  const schema = Joi.object({
    username: Joi.string()
      .max(70)
      .pattern(/^\S.*$/)
      .message("Username should not start with a space")
      .required(),
    email: Joi.string()
      .email({ minDomainSegments: 2, tlds: { allow: ['com', 'net'] } })
      .pattern(/^\S*$/)
      .message("Password should not contain spaces")
      .required(),
    password: Joi.string()
      .min(8)
      .pattern(/^\S*$/)
      .message("Password should not contain spaces")
      .required(),
    confirmPassword: Joi.string()
      .valid(Joi.ref("password"))
      .trim()
      .required()
      .strict()
  });

  return schema.validate(data);
};

// Route to Sign Up
router.post('/', async (req, res) => {
  const { username, email, password } = req.body;

  // Validate user input
  const { error } = validateUserInput(req.body);
  if (error) {
    return res.status(400).json({
      code: 400,
      error: error.details[0].message
    });
  }

  try {
    // Check if users with the same email are already registered
    const userSnapshot = await db.collection('users').where('email', '==', email).get();
    if (!userSnapshot.empty) {
      return res.status(400).json({
        code: 400,
        error: error,
        message: "Email already exists"
      });
    }

    // Hash password using bcrypt
    const hashedPassword = await bcrypt.hash(password, 10);

    // Generate new ID
    const userId = db.collection('users').doc().id;
    
    // Logging log messages to userLog
    userLog.info('SIGNED UP', { userId, username, email });

    const userData = {
      userId: userId,
      username: username,
      email: email,
      password: hashedPassword
    };

    // Save user data to Firestore with generated IDs
    await db.collection('users').doc(userId).set(userData);
    
    return res.status(200).json({
      code: 200,
      error: false,
      message: 'User Created',
      userData: userData
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: 'Failed to create user'
    });
  }
});

module.exports = router;
