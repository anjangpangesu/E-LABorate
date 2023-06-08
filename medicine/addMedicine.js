const express = require('express');
const Joi = require('joi');
const admin = require('firebase-admin');
const verifyPrivateKey = require('../private/keyValidator');
const router = express.Router();
const db = admin.firestore();

// Validate medicine input using Joi
const validateMedicineInput = (data) => {
  const schema = Joi.object({
    name: Joi.string()
      .regex(/^\S.*$/)
      .message("Name should not start with a space")
      .required(),
    description: Joi.string()
      .max(300)
      .regex(/^\S.*$/)
      .message("Description should not start with a space")
      .required(),
    price: Joi.number().required(),
    stock: Joi.number().integer().required()
  });

  return schema.validate(data);
};

// Route to add a new medicine
router.post('/:key/add-medicine', async (req, res) => {
  const key = req.params.key;
  const { name, description, price, stock } = req.body;

  // Verify private key
  const isPrivateKeyValid = await verifyPrivateKey(key);
  if (!isPrivateKeyValid) {
    return res.status(401).json({
      code: 400,
      error: true,
      message: 'Invalid private key',
    });
  }

  // Validate medicine input
  const { error } = validateMedicineInput(req.body);
  if (error) {
    return res.status(400).json({
      code: 400,
      error: error.details[0].message
    });
  }

  try {
    // Generate new ID
    const newMedicineId = db.collection('medicines').doc().id;
    const medicineData = {
      medicineId: newMedicineId,
      name: name,
      description: description,
      price: price,
      stock: stock
    };

    // Save medicine data to Firestore with generated ID
    await db.collection('medicines').doc(newMedicineId).set(medicineData);

    return res.status(200).json({
      code: 200,
      error: false,
      message: 'Medicine data is successfully added',
      medicineData: medicineData
    });
  } catch (error) {
    return res.status(500).json({
      code: 400,
      error: error,
      message: 'Medicine data failed to add'
    });
  }
});

module.exports = router;