const express = require('express');
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
        error: true,
        message: 'User not found',
      };
    }

    const userDoc = userQuery.docs[0]; // Retrieving the first document found
    const userData = userDoc.data();

    // Check if user is authenticated (has token)
    if (!userData.token) {
      return {
        error: true,
        message: 'User not authenticated',
      };
    }

    return {
      error: false,
      message: 'User profile retrieved',
      userData: {
        id: userData.id,
        username: userData.username,
        email: userData.email,
        phone: userData.phone,
        address: userData.address,
        token: userData.token,
      },
    };
  } catch (error) {
    return {
      error: true,
      message: 'Error retrieving user profile',
    };
  }
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
router.get('/profile/:id', async (req, res) => {
  const userId = req.params.id;
  const result = await getUserData(userId);

  if (result.error) {
    return res.status(404).json(result);
  }

  return res.status(200).json(result);
});

// Route to add users' phone numbers and addresses
router.post('/profile/edit=:id', async (req, res) => {
  const { username, email, phone, address } = req.body;
  const userId = req.params.id;
  const result = await getUserData(userId);

  if (result.error) {
    return res.status(404).json(result);
  }

  try {
    // Update user documents with newly added phone numbers and addresses
    await db.collection('users').doc(userId).update({
      username: username,
      email: email,
      phone: phone,
      address: address,
    });

    // Logging log messages using userLog
    userLog.info('PROFILE UPDATED', { userId, username, email, phone, address });

    return res.status(200).json({
      error: false,
      message: 'User profile updated successfully',
      id: userId,
      username: username,
      email: email,
      phone: phone,
      address: address,
    });
  } catch (error) {
    return res.status(500).json({
      error: true,
      message: 'Error updating user profile',
    });
  }
});

module.exports = router;
