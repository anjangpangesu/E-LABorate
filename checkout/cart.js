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

    const userData = userQuery.data();

    // Check if user is authenticated (has token)
    if (!userData.token) {
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
      userData: {
        userId: userData.userId,
        username: userData.username,
        email: userData.email,
        phone: userData.phone,
        address: userData.address,
        token: userData.token,
      },
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
        cartItems: [],
        userData: userResult.userData
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
      cartItems: cartItems,
      userData: userResult.userData
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