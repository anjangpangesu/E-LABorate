const express = require('express');
const { validateDiagnosisInput } = require('./validation');
const admin = require('firebase-admin');
const userLog = require('./../log/logger');

const router = express.Router();
const db = admin.firestore();

// Route to retrieve diagnosis result by ID
router.get('/results=:id', async (req, res) => {
  const diagnosisId = req.params.id;

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
      diagnosisId: diagnosisId,
      diagnosis: diagnosisData
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to retrieve diagnosis'
    });
  }
});

// Route to diagnose
router.post('/form', async (req, res) => {
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
      id: newDiagnosisId,
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
      diagnosisId: newDiagnosisId,
      diagnosis: diagnosisData
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to save diagnosis'
    });
  }
});

module.exports = router;
