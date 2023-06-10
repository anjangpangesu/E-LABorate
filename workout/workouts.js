const express = require('express');
const admin = require('firebase-admin');
const { extractUserFromToken } = require('./../auth/auth');
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

// Route to Get All Workouts
router.get('/:id/workout-list', async (req, res) => {
  try {
    const userId = req.params.id;
    const result = await getUserData(userId);

    if (result.error) {
      return res.status(404).json(result);
    }

    // Get all workouts from Firestore
    const workoutsSnapshot = await db.collection('workouts').get();

    // Create an empty array to store the workout data
    const workouts = [];

    // Loop through each workout document and extract the data
    workoutsSnapshot.forEach((doc) => {
      const workoutData = doc.data();
      workouts.push(workoutData);
    });

    // Map the medicines array to desired format
    const workoutData = workouts.map((workout) => ({
      workoutId: workout.workoutId,
      title: workout.title,
      videoLink: workout.videoLink
    }));

    const userData = result.userData;

    return res.status(200).json({
      code: 200,
      error: false,
      message: 'List of Workouts',
      userData: userData,
      workouts: workoutData
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: 'Failed to retrieve workouts'
    });
  }
});

module.exports = router;
