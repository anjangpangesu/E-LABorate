const express = require('express');
const Joi = require('joi');
const admin = require('firebase-admin');
const verifyPrivateKey = require('./../private/keyValidator');
const router = express.Router();
const db = admin.firestore();

// Validate workout input using Joi
const validateWorkoutInput = (data) => {
  const schema = Joi.object({
    title: Joi.string().trim().regex(/^[a-zA-Z0-9\s\p{P}#()]+$/u).required(),
    videoLink: Joi.string().uri().required()
  });

  return schema.validate(data);
};

// Route to add a new workout
router.post('/:key/add-workout', async (req, res) => {
  const key = req.params.key;
  const { title, videoLink } = req.body;

  // Verify private key
  const isPrivateKeyValid = await verifyPrivateKey(key);
  if (!isPrivateKeyValid) {
    return res.status(401).json({
      code: 401,
      error: true,
      message: 'Invalid private key',
    });
  }

  // Validate workout input
  const { error } = validateWorkoutInput(req.body);
  if (error) {
    return res.status(400).json({
      code: 400,
      error: error.details[0].message
    });
  }

  try {
    // Generate new ID
    const newWorkoutId = db.collection('workouts').doc().id;
    const workoutData = {
      workoutId: newWorkoutId,
      title: title,
      videoLink: videoLink
    };

    // Save workout's data to Firestore with generated ID
    await db.collection('workouts').doc(newWorkoutId).set(workoutData);

    return res.status(200).json({
      code: 200,
      error: false,
      message: 'Workout created',
      workoutData: workoutData
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: 'Failed to create workout'
    });
  }
});

module.exports = router;
