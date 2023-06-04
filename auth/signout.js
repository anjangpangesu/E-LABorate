const express = require('express');
const auth = require('./auth');
const admin = require('firebase-admin');
const userLog = require('../log/logger');

const router = express.Router();
const db = admin.firestore();

// Route to Sign Out
router.post('/', async (req, res) => {
  const { token } = req.body;

  try {
    // JWT token verification
    const decodedToken = await auth.verifyToken(token);
    
    // Retrieve user data from Firestore based on the email contained in the token
    const userQuery = await db.collection('users').where('id', '==', decodedToken.userId).get();
    if (userQuery.empty) {
      return res.status(404).json({
        error: true,
        message: "User not found"
      });
    }
    
    const userDoc = userQuery.docs[0]; // Retrieving the first document found
    const userData = userDoc.data();
    const username = userData.username;
    const email = userData.email;
    const phone = userData.phone;
    const address = userData.address;

    // Logging log messages to userLog
    userLog.info('SIGNED OUT', { userId: decodedToken.userId, username, email, phone, address });

    // Removing tokens from user documents in Firestore
    await userDoc.ref.update({
      token: admin.firestore.FieldValue.delete()
    });

    // Removing the token
    await auth.revokeToken(token);
    const tokenStat = auth.isTokenRevoked(token);

    return res.status(200).json({
      error: false,
      message: "User Signed Out",
      userId: decodedToken.userId,
      username: username,
      email: email,
      phone: phone,
      address: address,
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
