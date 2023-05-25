const express = require('express');
const auth = require('./auth');
const Joi = require('joi');
const admin = require('firebase-admin');
const userLog = require('../log/logger');

const router = express.Router();
const db = admin.firestore();

// Validasi input pengguna menggunakan Joi
const validateUserInput = (data) => {
  const schema = Joi.object({
    email: Joi.string()
      .email({ minDomainSegments: 2, tlds: { allow: ['com', 'net'] } })
      .trim()
      .required(),
    password: Joi.string().required()
  });

  return schema.validate(data);
};

// Route untuk Sign In
router.post('/', async (req, res) => {
  const { email, password } = req.body;

  // Validasi input pengguna
  const { error } = validateUserInput(req.body);
  if (error) {
    return res.status(400).json({
      error: error.details[0].message
    });
  }

  // Ambil data pengguna dari Firestore
  try {
    const userDoc = await db.collection('users').doc(email).get();
    if (!userDoc.exists) {
      return res.status(401).json({
        error: true,
        message: "Invalid email or password"
      });
    }

    const userData = userDoc.data();

    // Verifikasi password menggunakan bcrypt
    const passwordMatch = await auth.verifyPassword(password, userData.password);
    if (!passwordMatch) {
      return res.status(401).json({
        error: true,
        message: "Invalid email or password"
      });
    }

    // Buat token JWT
    const token = auth.generateToken(userData.email);
    // Simpan token ke dalam koleksi pengguna
    await db.collection('users').doc(email).update({
      token: token
    });

    // Mencatat pesan log menggunakan signinLogger
    userLog.info('SIGN IN', { email });

    return res.status(200).json({
      error: false,
      message: "User Signed In",
      userId: userData.id,
      email: userData.email,
      username: userData.username,
      token
    });
  } catch (error) {
    return res.status(500).json({
      error: true,
      message: "Error Signing In"
    });
  }
});

module.exports = router;