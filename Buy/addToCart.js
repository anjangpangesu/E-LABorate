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
        error: true,
        message: 'User not found',
      };
    }

    const userData = userQuery.data();

    // Check if user is authenticated (has token)
    if (!userData.token) {
      return {
        error: true,
        message: 'User not authenticated',
      };
    }

    return {
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
      error: error,
      message: 'Error retrieving user profile',
    };
  }
};

// Function to retrieve medicine data based on ID
const getMedicineData = async (medicineId) => {
  try {
    // Get medicine data using medicine ID
    const medicineQuery = await db.collection('medicines').doc(medicineId).get();

    if (!medicineQuery.exists) {
      return {
        error: true,
        message: 'Medicine not found',
      };
    }

    const medicineData = medicineQuery.data();

    return {
      error: false,
      message: 'Medicine data retrieved',
      medicineData: medicineData,
    };
  } catch (error) {
    return {
      error: error,
      message: 'Error retrieving medicine data',
    };
  }
};

// Route to Add Medicine to Cart
router.post('/:userId/add-to-cart/:medicineId', async (req, res) => {
  try {
    const userId = req.params.userId;
    const medicineId = req.params.medicineId;

    const userResult = await getUserData(userId);

    if (userResult.error) {
      return res.status(404).json(userResult);
    }

    const medicineResult = await getMedicineData(medicineId);

    if (medicineResult.error) {
      return res.status(404).json(medicineResult);
    }

    // Get user's cart data
    const cartQuery = await db.collection('carts').doc(userId).get();

    // Check if cart exists for the user
    if (!cartQuery.exists) {
      // Create a new cart for the user if it doesn't exist
      const newCartData = {
        userId: userId,
        medicines: [medicineResult.medicineData],
      };
      await db.collection('carts').doc(userId).set(newCartData);
    } else {
      // Update the existing cart for the user
      const cartData = cartQuery.data();
      cartData.medicines.push(medicineResult.medicineData);
      await db.collection('carts').doc(userId).update(cartData);
    }

    return res.status(200).json({
      error: false,
      message: 'Medicine added to cart successfully',
      userId: userResult.userData.userId,
      username: userResult.userData.username,
      token: userResult.userData.token,
      medicine: medicineResult.medicineData,
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to add medicine to cart',
    });
  }
});

module.exports = router;