const express = require('express');
const router = express.Router();
const {JWT_SECRET} = require('./auth');
const admin = require('firebase-admin');
const db = admin.firestore();
const jwt = require('jsonwebtoken');

// Route to Verify Code
router.post('/', async (req, res) => {
  const { email, verificationCode } = req.body;

  try {
    const userDoc = await db.collection('users').where('email', '==', email).get();
    if (userDoc.empty) {
      return res.status(404).json({
        code: 400,
        error: error,
        message: "User not found"
      });
    }

    const userData = userDoc.docs[0].data();
    const userId = userData.userId;

    if (userData.verificationCode !== verificationCode) {
      return res.status(401).json({
        code: 401,
        error: error,
        message: "Invalid verification code"
      });
    }

    // Generate token for password reset
    const resetToken = jwt.sign({ userId: userId }, JWT_SECRET, { expiresIn: '1h' });

    // Remove verificationCode field from the user document
    await db.collection('users').doc(userId).update({
      verificationCode: admin.firestore.FieldValue.delete()
    });

    return res.status(200).json({
      code: 200,
      error: false,
      message: "Token generated",
      userId: userId,
      username: userData.username,
      email: userData.email,
      resetToken: resetToken,
      verificationCodeDeleted: true
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: "Error verifying code"
    });
  }
});

module.exports = router;