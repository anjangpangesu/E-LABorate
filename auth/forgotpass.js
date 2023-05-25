const express = require('express');
const router = express.Router();
const nodemailer = require('nodemailer');
const admin = require('firebase-admin');

const db = admin.firestore();

// Konfigurasi Nodemailer
const transporter = nodemailer.createTransport({
  host: "sandbox.smtp.mailtrap.io",
  port: 2525,
  auth: {
    user: "6fa8684e60cf17",
    pass: "ad5720c3e911c7"
  }
});

// Fungsi helper untuk mengirim email
const sendEmail = async (email, subject, message) => {
  const mailOptions = {
    from: 'anjangpangestu2304@gmail.com',
    to: email,
    subject: subject,
    text: message
  };

  await transporter.sendMail(mailOptions);
};

// Route untuk Forget Password
router.post('/', async (req, res) => {
  const { email } = req.body;

  try {
    const userDoc = await db.collection('users').doc(email).get();
    if (!userDoc.exists) {
      return res.status(404).json({
        error: true,
        message: "User not found"
      });
    }

    // Generate verification code
    const verificationCode = Math.floor(100000 + Math.random() * 900000);

    // Save verification code to Firestore
    await db.collection('users').doc(email).update({
      verificationCode: verificationCode.toString()
    });

    // Send verification code via email
    const emailSubject = 'Verification Code';
    const emailMessage = `Your verification code is: ${verificationCode}`;

    await sendEmail(email, emailSubject, emailMessage);

    return res.status(200).json({
      error: false,
      message: "Verification code has been sent",
      email: email
    });
  } catch (error) {
    return res.status(500).json({
      error: true,
      message: "Failed to send verification code"
    });
  }
});

module.exports = router;