const express = require('express');
const { validateDiagnosisInput } = require('./validation');
const admin = require('firebase-admin');
const userLog = require('./../log/logger');
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

// Route to diagnose
router.post('/:id/diagnose', async (req, res) => {
  const userId = req.params.id;
  const result = await getUserData(userId);

  if (result.error) {
    return res.status(404).json(result);
  }

  const {
    age, sex, rbc,
    hgb, hct, mcv,
    mch, mchc, rdw_cv,
    wbc, neu, lym,
    mo, eos, ba
  } = req.body;

  // Validate user input
  const { error } = validateDiagnosisInput(req.body);
  if (error) {
    return res.status(400).json({
      error: error.details[0].message
    });
  }

  try {
    // Generate new ID for the document
    const newDiagnosisId = db.collection('results').doc().id;

    const diagnosisData = {
      diagnosisId: newDiagnosisId,
      age: age,
      sex: sex,
      rbc: {
        value: rbc.value,
        units: rbc.units
      },
      hgb: {
        value: hgb.value,
        units: hgb.units
      },
      hct: {
        value: hct.value,
        units: hct.units
      },
      mcv: {
        value: mcv.value,
        units: mcv.units
      },
      mch: {
        value: mch.value,
        units: mch.units
      },
      mchc: {
        value: mchc.value,
        units: mchc.units
      },
      rdw_cv: {
        value: rdw_cv.value,
        units: rdw_cv.units
      },
      wbc: {
        value: wbc.value,
        units: wbc.units
      },
      neu: {
        value: neu.value,
        units: neu.units
      },
      lym: {
        value: lym.value,
        units: lym.units
      },
      mo: {
        value: mo.value,
        units: mo.units
      },
      eos: {
        value: eos.value,
        units: eos.units
      },
      ba: {
        value: ba.value,
        units: ba.units
      },
    }

    // Save diagnosis data to Firestore with generated ID
    await db.collection('results').doc(newDiagnosisId).set(diagnosisData);

    // Logging log messages to userLog
    userLog.info('DIAGNOSIS', { diagnosisId: newDiagnosisId });

    return res.status(200).json({
      error: false,
      message: 'Diagnosis saved',
      userId: result.userData.userId,
      username: result.userData.username,
      token: result.userData.token,
      diagnosisData: diagnosisData
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to save diagnosis'
    });
  }
});

// Route to retrieve diagnosis result by ID
router.get('/:id/diagnose?results=:diagnosisId', async (req, res) => {
  const diagnosisId = req.params.diagnosisId;
  const userId = req.params.id;
  const result = await getUserData(userId);

  if (result.error) {
    return res.status(404).json(result);
  }

  try {
    // Retrieve diagnosis data from Firestore
    const diagnosisSnapshot = await db.collection('results').doc(diagnosisId).get();

    if (!diagnosisSnapshot.exists) {
      return res.status(404).json({
        error: true,
        message: 'Diagnosis not found'
      });
    }

    const diagnosisData = diagnosisSnapshot.data();

    return res.status(200).json({
      error: false,
      message: 'Diagnosis retrieved',
      userId: result.userData.userId,
      username: result.userData.username,
      token: result.userData.token,
      diagnosis: diagnosisData
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to retrieve diagnosis'
    });
  }
});

module.exports = router;
