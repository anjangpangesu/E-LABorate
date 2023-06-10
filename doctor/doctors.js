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

    const userData = userQuery.data();

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
      code: 500,
      error: error,
      message: 'Error retrieving user profile',
    };
  }
};

// Route to Get All Doctors
router.get('/:id/doctor-list', async (req, res) => {
  try {
    const userId = req.params.id;
    const result = await getUserData(userId);

    if (result.error) {
      return res.status(404).json(result);
    }

    // Get all doctors from Firestore
    const doctorsSnapshot = await db.collection('doctors').get();

    // Create an empty array to store the doctor data
    const doctors = [];
    const userData = result.userData;

    // Loop through each doctor document and extract the data
    doctorsSnapshot.forEach((doc) => {
      const doctorData = doc.data();
      doctors.push(doctorData);
    });

    // Map the doctors array to desired format
    const doctorsData = doctors.map((doctor) => ({
      doctorId: doctor.doctorId,
      name: doctor.name,
      age: doctor.age,
      gender: doctor.gender,
      specialty: doctor.specialty,
      workplace: doctor.workplace,
      experiences: doctor.experiences
    }));

    return res.status(200).json({
      code: 200,
      error: false,
      message: 'List of Doctors',
      userData: userData,
      doctorsData: doctorsData
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: 'Failed to retrieve doctors'
    });
  }
});

module.exports = router;
