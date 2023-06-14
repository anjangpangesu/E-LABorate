const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const Joi = require('joi');
const auth = require('./auth');
const admin = require('firebase-admin');
const {JWT_SECRET} = require('./auth');
const jwt = require('jsonwebtoken');
const { promisify } = require('util');
const verify = promisify(jwt.verify);
const userLog = require('../log/logger');
const db = admin.firestore();

// Validate user input using Joi
const validateUserInput = (data) => {
  const schema = Joi.object({
    resetToken: Joi.string()
      .trim()
      .message("Token should not contain spaces")
      .required(),
    newPassword: Joi.string()
      .min(8)
      .pattern(/^\S*$/)
      .message("Password should not contain spaces")
      .required()
  });

  return schema.validate(data);
};

// Route untuk Reset Password
router.post('/', async (req, res) => {
  const { resetToken, newPassword } = req.body;
  
  // Validate user input
  const { error } = validateUserInput(req.body);
  if (error) {
    return res.status(400).json({
      code: 400,
      error: error.details[0].message
    });
  }

  try {
    const decoded = await verify(resetToken, JWT_SECRET);
    const userId = decoded.userId;

    const userDoc = await db.collection('users').doc(userId).get();
    const userData = userDoc.data()

    // Hash password yang baru
    const hashedPassword = await bcrypt.hash(newPassword, 10);

    // Memperbarui password di Firestore
    await db.collection('users').doc(userId).update({
      password: hashedPassword
    });

    // Removing the token
    await auth.revokeToken(resetToken);
    const tokenStat = auth.isTokenRevoked(resetToken);

    // Logging log messages to userLog
    userLog.info('PASSWORD RESET', { userId, username: userData.username, email: userData.email, phone: userData.phone, address: userData.address });

    return res.status(200).json({
      code: 200,
      error: false,
      message: "Password has been reset",
      userId: userId,
      username: userData.username,
      email: userData.email,
      tokenRevoked: tokenStat
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: "Failed to reset password"
    });
  }
});

module.exports = router;