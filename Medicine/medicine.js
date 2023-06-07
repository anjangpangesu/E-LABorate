const express = require('express');
const admin = require('firebase-admin');
const { extractUserFromToken } = require('../auth/auth');
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

    const userData = userQuery.data();

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
        userId: userData.userId,
        username: userData.username,
        email: userData.email,
        phone: userData.phone,
        address: userData.address,
        token: userData.token,
      },
    };
  } catch (error) {
    return {
      error: error,
      message: 'Error retrieving user profile',
    };
  }
};

// Route to Get All Medicines
router.get('/:id/medicine-list', async (req, res) => {
  try {
    const userId = req.params.id;
    const result = await getUserData(userId);

    if (result.error) {
      return res.status(404).json(result);
    }

    // Get all medicines from Firestore
    const medicinesSnapshot = await db.collection('medicines').get();

    // Create an empty array to store the medicine data
    const medicines = [];

    // Loop through each medicine document and extract the data
    medicinesSnapshot.forEach((doc) => {
      const medicineData = doc.data();
      medicines.push(medicineData);
    });

    return res.status(200).json({
      error: false,
      message: 'List of Medicines',
      userId: result.userData.userId,
      username: result.userData.username,
      token: result.userData.token,
      medicines: medicines
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to retrieve medicines'
    });
  }
});

module.exports = router;