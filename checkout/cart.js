const express = require('express');
const admin = require('firebase-admin');
const { extractUserFromToken } = require('./../auth/auth');
const router = express.Router();
const db = admin.firestore();

// Middleware to extract user information from JWT token
router.use(extractUserFromToken);

// Function to retrieve user profile based on ID
const getUserData = async (userId) => {
  try {
    // Get user data using user ID
    const userQuery = await db.collection('users').doc(userId).get();

    if (!userQuery.exists) {
      return {
        code: 404,
        error: true,
        message: 'User not found',
      };
    }

    const userDocData = userQuery.data();
    const userData = {
      userId: userDocData.userId,
      diagnosisId: userDocData.diagnosisId,
      username: userDocData.username,
      email: userDocData.email,
      phone: userDocData.phone,
      address: userDocData.address,
      token: userDocData.token,
    }

    // Check if user is authenticated (has token)
    if (!userDocData.token) {
      return {
        code: 404,
        error: true,
        message: 'User not authenticated',
      };
    }

    return {
      code: 200,
      error: false,
      message: 'User profile retrieved',
      userData: userData
    };
  } catch (error) {
    return {
      code: 500,
      error: error,
      message: 'Error retrieving user profile',
    };
  }
};

// Route to Get Cart Items
router.get('/:userId/cart', async (req, res) => {
  try {
    const userId = req.params.userId;
    const userResult = await getUserData(userId);

    if (userResult.error) {
      return res.status(404).json(userResult);
    }

    // Get user's cart data
    const cartQuery = await db.collection('carts').doc(userId).get();

    // Check if cart exists for the user
    if (!cartQuery.exists) {
      return res.status(404).json({
        code: 404,
        error: false,
        message: 'Cart is empty',
        userData: userResult.userData,
        cartItems: []
      });
    }

    const cartData = cartQuery.data();

    // Map the cart items to desired format
    const cartItems = cartData.medicines.map((medicine) => ({
      medicineId: medicine.medicineId,
      name: medicine.name,
      description: medicine.description,
      price: medicine.price,
      stock: medicine.stock
    }));

    return res.status(200).json({
      code: 200,
      error: false,
      message: 'Cart items retrieved',
      userData: userResult.userData,
      cartItems: cartItems
    });
  } catch (error) {
    return res.status(500).json({
      code: 500,
      error: error,
      message: 'Failed to retrieve cart items',
    });
  }
});

module.exports = router;