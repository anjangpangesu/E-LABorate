const express = require('express');
const Joi = require('joi');
const admin = require('firebase-admin');
const verifyPrivateKey = require('./../private/keyValidator');
const router = express.Router();
const db = admin.firestore();

// Validate doctor input using Joi
const validateDoctorInput = (data) => {
  const schema = Joi.object({
    name: Joi.string()
      .pattern(/^[a-zA-Z\s\p{P}]+$/u)
      .required(),
    age: Joi.number()
      .integer()
      .required(),
    gender: Joi.string()
      .valid('Male', 'Female')
      .required(),
    specialty: Joi.string().required(),
    workplace: Joi.array()
      .items(Joi.string()
      .pattern(/^[a-zA-Z\s\p{P}]+$/u))
      .required(),
    experiences: Joi.array()
      .items(Joi.string()
      .pattern(/^[\w\s().-]+$/u))
      .required()
  });

  return schema.validate(data);
};

// Route to add a new doctor
router.post('/:key/add-doctor', async (req, res) => {
  const key = req.params.key;
  const { name, age, gender, specialty, workplace, experiences } = req.body;

  // Verify private key
  const isPrivateKeyValid = await verifyPrivateKey(key);
  if (!isPrivateKeyValid) {
    return res.status(401).json({
      code: 401,
      error: true,
      message: 'Invalid private key',
    });
  }

  // Validate doctor input
  const { error } = validateDoctorInput(req.body);
  if (error) {
    return res.status(400).json({
      code: 400,
      error: error.details[0].message
    });
  }

  try {
    // Generate new ID
    const newDoctorId = db.collection('doctors').doc().id;
    const doctorData = {
      doctorId: newDoctorId,
      name: name,
      age: age,
      gender: gender,
      specialty: specialty,
      workplace: workplace,
      experiences: experiences
    };

    // Save doctor's data to Firestore with generated ID
    await db.collection('doctors').doc(newDoctorId).set(doctorData);

    return res.status(200).json({
      code: 200,
      error: false,
      message: 'Doctor Created',
      doctorData: doctorData
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: 'Failed to create doctor'
    });
  }
});

module.exports = router;
