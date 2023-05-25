const express = require('express');
const auth = require('./auth');
const admin = require('firebase-admin');
const userLog = require('../log/logger');

const router = express.Router();
const db = admin.firestore();

// Route untuk Sign Out
router.post('/', async (req, res) => {
  const { token } = req.body;

  try {
    // Verifikasi token JWT
    const decodedToken = await auth.verifyToken(token);

    // Ambil data pengguna dari Firestore berdasarkan email yang terkandung dalam token
    const userDoc = await db.collection('users').doc(decodedToken.email).get();
    if (!userDoc.exists) {
      return res.status(404).json({
        error: true,
        message: "User not found"
      });
    }

    // Mencatat pesan log menggunakan signoutLogger
    userLog.info('SIGN OUT', { email: decodedToken.email });

    // Menghapus token dari dokumen pengguna di Firestore
    await userDoc.ref.update({
      token: admin.firestore.FieldValue.delete()
    });

    // Merevoke token
    auth.revokeToken(token);
    const tokenStat = auth.isTokenRevoked(token);

    return res.status(200).json({
      error: false,
      message: "User Signed Out",
      email: decodedToken.email,
      tokenRevoked: tokenStat
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: "Error Signing Out"
    });
  }
});

module.exports = router;
