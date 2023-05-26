const express = require('express');
const admin = require('firebase-admin');
const userLog = require('../log/logger');

const router = express.Router();
const db = admin.firestore();

// Route to get user profile data based on id
router.get('/:id', async (req, res) => {
  try {
    const userId = req.params.id;

    // Get user email using user id
    const userQuery = await db.collection('users').where('id', '==', userId).get();

    if (userQuery.empty) {
      return res.status(404).json({
        error: error,
        message: 'User not found',
      });
    }

    const userDoc = userQuery.docs[0]; // Retrieving the first document found
    const userEmail = userDoc.data().email;

    // Ambil data pengguna berdasarkan email
    const userDataQuery = await db.collection('users').where('email', '==', userEmail).get();

    if (userDataQuery.empty) {
      return res.status(404).json({
        error: error,
        message: 'User not found',
      });
    }

    const userDataDoc = userDataQuery.docs[0]; // Retrieve the first document found
    const userData = userDataDoc.data();

    return res.status(200).json({
      error: false,
      message: 'User profile retrieved',
      userId: userData.id,
      username: userData.username,
      email: userData.email,
      token: userData.token,
      phone: userData.phone,
      address: userData.address,
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Error retrieving user profile',
    });
  }
});

// Route to add users' phone numbers and addresses
router.post('/:id', async (req, res) => {
  const { phone, address } = req.body;

  try {
    const userId = req.params.id;

    // Get user email using user id
    const userQuery = await db.collection('users').where('id', '==', userId).get();

    if (userQuery.empty) {
      return res.status(404).json({
        error: error,
        message: 'User not found',
      });
    }

    const userDoc = userQuery.docs[0]; // Retrieve the first document found
    const userEmail = userDoc.data().email;

    // Update user documents with newly added phone numbers and addresses
    await db.collection('users').doc(userEmail).update({
      phone: phone,
      address: address,
    });

    // Logging log messages using userLog
    userLog.info('User profile updated', { email: userEmail });

    return res.status(200).json({
      error: false,
      message: 'User profile updated successfully',
      id: userId,
      email: userEmail,
      phone: phone,
      address: address
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Error updating user profile',
    });
  }
});

module.exports = router;
