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
    const userQuery = await db.collection('users').where('userId', '==', decodedToken.userId).get();
    if (userQuery.empty) {
      return res.status(404).json({
        code: 404,
        error: true,
        message: "User not found"
      });
    }

    // Retrieving the first document found
    const userDoc = userQuery.docs[0];
    const userDocData = userDoc.data();
    const userId = decodedToken.userId;
    const username = userDocData.username;
    const email = userDocData.email;
    const phone = userDocData.phone;
    const address = userDocData.address;

    // Removing tokens from user documents in Firestore
    await userDoc.ref.update({
      token: admin.firestore.FieldValue.delete()
    });
    
    // Removing the token
    await auth.revokeToken(token);
    const tokenStat = auth.isTokenRevoked(token);

    // Logging log messages to userLog
    userLog.info('SIGNED OUT', { userId, username, email, phone, address });

    const userData = {
      userId: userId,
      username: username,
      email: email,
      phone: phone,
      address: address,
      tokenRevoked: tokenStat
    }

    return res.status(200).json({
      code: 200,
      error: false,
      message: "User Signed Out",
      userData: userData
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: "Error Signing Out"
    });
  }
});

module.exports = router;
