const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const admin = require('firebase-admin');
const {JWT_SECRET} = require('./auth');
const jwt = require('jsonwebtoken');
const { promisify } = require('util');
const verify = promisify(jwt.verify);
const userLog = require('../log/logger');

const db = admin.firestore();

// Route untuk Reset Password
router.post('/', async (req, res) => {
  const { resetToken, newPassword } = req.body;
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

      // Logging log messages to userLog
    userLog.info('PASSWORD RESET', { userId, username: userData.username, email: userData.email, phone: userData.phone, address: userData.address });
  
      return res.status(200).json({
        error: false,
        message: "Password has been reset",
        userId: userId,
        username: userData.username,
        email: userData.email
      });
    } catch (error) {
      return res.status(500).json({
        error: error,
        message: "Failed to reset password"
      });
    }
});
  
module.exports = router;