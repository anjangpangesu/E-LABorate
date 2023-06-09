const express = require('express');
const fs = require('fs');
const path = require('path');
const router = express.Router();
const nodemailer = require('nodemailer');
const admin = require('firebase-admin');
const db = admin.firestore();
require('dotenv').config({ path: '../SECRET_KEY.env' });
const PASS = process.env.PASS;

// Konfigurasi Nodemailer
const transporter = nodemailer.createTransport({
  host: 'smtp.gmail.com',
  port: 465,
  secure: true,
  auth: {
    user: 'elaborateapp.id@gmail.com',
    pass: PASS
  }
});

// Fungsi helper untuk mengirim email
const sendEmail = async (email, subject, message) => {
  const mailOptions = {
    from: 'elaborateapp.id@gmail.com',
    to: email,
    subject: subject,
    html: message
  };
  await transporter.sendMail(mailOptions);
};

// Route untuk Forget Password
router.post('/', async (req, res) => {
  const { email } = req.body;
  const teamEmail = 'elaborateapp.id@gmail.com';

  try {
    const userDoc = await db.collection('users').where('email', '==', email).get();
    if (userDoc.empty) {
      return res.status(404).json({
        code: 404,
        error: true,
        message: "User not found"
      });
    }

    const userData = userDoc.docs[0].data();
    const userId = userData.userId;
    const username = userData.username;
    
    // Generate verification code
    const verificationCode = Math.floor(100000 + Math.random() * 900000);
    
    // Save verification code to Firestore
    await db.collection('users').doc(userId).update({
      verificationCode: verificationCode.toString()
    });
    
    // Send verification code via email
    const emailSubject = 'Your Verification Code';
    let emailMessage = fs.readFileSync(path.join(__dirname, 'message/message.html'), 'utf8');

    // Replace the placeholder with the verification code
    emailMessage = emailMessage.replace('{{email}}', email)
      .replace('{{teamEmail}}', teamEmail)
      .replace('{{verificationCode}}', verificationCode.toString());
    
    await sendEmail(email, emailSubject, emailMessage);

    return res.status(200).json({
      code: 200,
      error: false,
      message: "Verification code has been sent",
      userId: userId,
      username: username,
      email: email,
      verificationCode: verificationCode.toString()
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: "Failed to send verification code"
    });
  }
});

module.exports = router;