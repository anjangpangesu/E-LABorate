const express = require('express');
const Joi = require('joi');
const admin = require('firebase-admin');
const { extractUserFromToken } = require('./auth');
const userLog = require('../log/logger');
const router = express.Router();
const db = admin.firestore();

// Middleware to extract user information from JWT token
router.use(extractUserFromToken);

// Function to retrieve user profiles based on ID
const getUserData = async (userId) => {
  try {
    // Get user data using user ID
    const userQuery = await db.collection('users').doc(userId).get();

    if (!userQuery.exists) {
      return {
        code: 404,
        error: true,
        message: 'User not found',
      };
    }

    const userDocData = userQuery.data();
    const userData = {
      userId: userDocData.userId,
      username: userDocData.username,
      email: userDocData.email,
      phone: userDocData.phone,
      address: userDocData.address,
      token: userDocData.token,
    }

    // Check if user is authenticated (has token)
    if (!userData.token) {
      return {
        code: 404,
        error: true,
        message: 'User not authenticated',
      };
    }

    return {
      code: 200,
      error: false,
      message: 'User profile retrieved',
      userData: userData
    };
  } catch (error) {
    return {
      code: 500,
      error: error,
      message: 'Error retrieving user profile',
    };
  }
};

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
      .required(),
    phone: Joi.string()
      .allow('')
      .min(10)
      .max(15)
      .pattern(/^\d+$/)
      .message("Phone number should only contain digits"),
    address: Joi.string()
      .allow('')
      .pattern(/^\S.*$/)
      .message("Address should not start with a space")
  });

  return schema.validate(data);
};

// Route to get user profile data based on ID
router.get('/:id', async (req, res) => {
  const userId = req.params.id;
  const result = await getUserData(userId);

  if (result.error) {
    return res.status(404).json(result);
  }

  return res.status(200).json(result);
});

// Route to get user profile data based on ID for profile endpoint
router.get('/:id/profile', async (req, res) => {
  const userId = req.params.id;
  const result = await getUserData(userId);

  if (result.error) {
    return res.status(404).json(result);
  }

  return res.status(200).json(result);
});

// Route to add users' phone numbers and addresses
router.post('/:id/profile/edit', async (req, res) => {
  const { username, email, phone, address } = req.body;
  const userId = req.params.id;
  const result = await getUserData(userId);

  if (result.error) {
    return res.status(404).json(result);
  }

  // Validate user input
  const { error } = validateUserInput(req.body);
  if (error) {
    return res.status(400).json({
      code: 400,
      error: true,
      message: error.details[0].message
    });
  }

  try {
    // Update user documents with newly added phone numbers and addresses
    await db.collection('users').doc(userId).update({
      username: username,
      email: email,
      phone: phone,
      address: address,
    });

    const userData = {
      userId: userId,
      username: username,
      email: email,
      phone: phone,
      address: address
    }

    // Logging log messages using userLog
    userLog.info('PROFILE UPDATED', { userId, username, email, phone, address });

    return res.status(200).json({
      code: 200,
      error: false,
      message: 'User profile updated successfully',
      userData: userData
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: true,
      message: 'Error updating user profile',
    });
  }
});

module.exports = router;
