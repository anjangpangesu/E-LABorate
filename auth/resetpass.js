const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const admin = require('firebase-admin');
const {JWT_SECRET} = require('./auth');
const jwt = require('jsonwebtoken');
const { promisify } = require('util');
const verify = promisify(jwt.verify);

const db = admin.firestore();

// Route untuk Reset Password
router.post('/', async (req, res) => {
  const { token, newPassword } = req.body;
    try {
      const decoded = await verify(token, JWT_SECRET);
      const email = decoded.email;
  
      // Hash password yang baru
      const hashedPassword = await bcrypt.hash(newPassword, 10);
  
      // Memperbarui password di Firestore
      await db.collection('users').doc(email).update({
        password: hashedPassword
      });
  
      return res.status(200).json({
        error: false,
        message: "Password has been reset",
        email: email
      });
    } catch (error) {
      return res.status(500).json({
        error: error,
        message: "Failed to reset password"
      });
    }
});
  
module.exports = router;