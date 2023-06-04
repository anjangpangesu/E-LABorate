const express = require('express');
const admin = require('firebase-admin');

const router = express.Router();
const db = admin.firestore();

// Route to Get All Doctors
router.get('/:id/doctor-list', async (req, res) => {
  try {
    // Get all doctors from Firestore
    const doctorsSnapshot = await db.collection('doctors').get();

    // Create an empty array to store the doctor data
    const doctors = [];

    // Loop through each doctor document and extract the data
    doctorsSnapshot.forEach((doc) => {
      const doctorData = doc.data();
      doctors.push(doctorData);
    });

    return res.status(200).json({
      error: false,
      message: 'List of Doctors',
      doctors: doctors
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to retrieve doctors'
    });
  }
});

module.exports = router;
