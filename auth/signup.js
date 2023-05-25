const express = require('express');
const bcrypt = require('bcrypt');
const Joi = require('joi');
const admin = require('firebase-admin');
const userLog = require('./../log/logger');

const router = express.Router();
const db = admin.firestore();

// Validasi input pengguna menggunakan Joi
const validateUserInput = (data) => {
  const schema = Joi.object({
    username: Joi.string()
      .max(70)
      .pattern(/^\S.*$/)
      .message("Username should not start with a space")
      .required(),
    email: Joi.string()
      .email({ minDomainSegments: 2, tlds: { allow: ['com', 'net'] } })
      .trim()
      .required(),
    password: Joi.string()
      .min(8)
      .pattern(/^\S*$/)
      .message("Password should not contain spaces")
      .required(),
    confirmPassword: Joi.string()
      .valid(Joi.ref("password"))
      .trim()
      .required()
      .strict()
  });

  return schema.validate(data);
};

// Route untuk Sign Up
router.post('/', async (req, res) => {
  const { username, email, password } = req.body;

  // Validasi input pengguna
  const { error } = validateUserInput(req.body);
  if (error) {
    return res.status(400).json({
      error: error.details[0].messag
    });
  }

  try {
    // Cek apakah pengguna dengan email yang sama sudah terdaftar
    const userSnapshot = await db.collection('users').where('email', '==', email).get();
    if (!userSnapshot.empty) {
      return res.status(400).json({
        error: true,
        message: "Email already exists"
      });
    }

    // Hash password menggunakan bcrypt
    const hashedPassword = await bcrypt.hash(password, 10);

    // Mencatat pesan log menggunakan signupLogger
    userLog.info('SIGN UP', { email });

    // Generate ID baru dengan metode doc() dari Firestore
    const newUserId = db.collection('users').doc().id;

    // Simpan data pengguna ke Firestore dengan ID yang dihasilkan
    await db.collection('users').doc(email).set({
      id: newUserId,
      username: username,
      email: email,
      password: hashedPassword
    });
    
    return res.status(200).json({
      error: false,
      message: 'User Created',
      userId: newUserId,
      username: username
    });
  } catch (error) {
    return res.status(500).json({
      error: true,
      message: 'Failed to create user'
    });
  }
});

module.exports = router;
