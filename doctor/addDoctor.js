const express = require('express');
const Joi = require('joi');
const admin = require('firebase-admin');

const router = express.Router();
const db = admin.firestore();

// Validate doctor input using Joi
const validateDoctorInput = (data) => {
  const schema = Joi.object({
    name: Joi.string().pattern(/^[a-zA-Z\s\p{P}]+$/u).required(),
    age: Joi.number().integer().required(),
    gender: Joi.string().valid('Male', 'Female').required(),
    specialist: Joi.string().required(),
    work_place: Joi.array().items(Joi.string().pattern(/^[a-zA-Z\s\p{P}]+$/u)).required(),
    experiences: Joi.number().integer().required()
  });

  return schema.validate(data);
};

// Route to add a new doctor
router.post('/', async (req, res) => {
  const { name, age, gender, specialist, work_place, experiences } = req.body;

  // Validate doctor input
  const { error } = validateDoctorInput(req.body);
  if (error) {
    return res.status(400).json({
      error: error.details[0].message
    });
  }

  try {
    // Generate new ID
    const newDoctorId = db.collection('doctors').doc().id;

    // Save doctor data to Firestore with generated ID
    await db.collection('doctors').doc(newDoctorId).set({
      id: newDoctorId,
      name: name,
      age: age,
      gender: gender,
      specialist: specialist,
      work_place: work_place,
      experiences: experiences
    });

    return res.status(200).json({
      error: false,
      message: 'Doctor Created',
      doctorId: newDoctorId,
      name: name,
      age: age,
      gender: gender,
      specialist: specialist,
      work_place: work_place,
      experiences: experiences
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to create doctor'
    });
  }
});

module.exports = router;
