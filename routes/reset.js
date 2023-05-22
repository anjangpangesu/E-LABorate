const express = require('express');
const jwt = require('jsonwebtoken');
const auth = require('./auth');
const nodemailer = require('nodemailer');
const admin = require('firebase-admin');

const router = express.Router();
const db = admin.firestore();

// Konfigurasi Nodemailer
const transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: 'anjangpangestu312@gmail.com',
    pass: 'ANJANG_23'
  }
});

// Fungsi helper untuk mengirim email
const sendEmail = async (email, subject, message) => {
  const mailOptions = {
    from: 'anjangpangestu312@gmail.com',
    to: email,
    subject: subject,
    text: message
  };

  await transporter.sendMail(mailOptions);
};

// Route untuk Forget Password
router.post('/', async (req, res) => {
  const { email } = req.body;

  // Cek apakah email pengguna ada di Firestore
  try {
    const userDoc = await db.collection('users').doc(email).get();
    if (!userDoc.exists) {
      return res.status(404).send('User not found');
    }

    // Buat token JWT yang akan dikirimkan ke email pengguna
    const token = jwt.sign({ email: email }, auth.JWT_SECRET, { expiresIn: '1h' });

    // Kirim email dengan link untuk mereset password
    const resetPasswordLink = `http://localhost:8080/resetpassword?token=${token}`;
    const emailSubject = 'Reset Password';
    const emailMessage = `Click the following link to reset your password: ${resetPasswordLink}`;

    await sendEmail(email, emailSubject, emailMessage);

    res.sendStatus(200);
  } catch (error) {
    console.error('Error sending reset password email:', error);
    res.sendStatus(500);
  }
});

module.exports = router;