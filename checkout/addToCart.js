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

// Function to retrieve medicine data based on ID
const getMedicineData = async (medicineId) => {
  try {
    // Get medicine data using medicine ID
    const medicineQuery = await db.collection('medicines').doc(medicineId).get();

    if (!medicineQuery.exists) {
      return {
        code: 404,
        error: true,
        message: 'Medicine not found',
      };
    }

    const medicineData = medicineQuery.data();
    const orderedMedicineData = {
      medicineId: medicineData.medicineId,
      name: medicineData.name,
      description: medicineData.description,
      price: medicineData.price,
      stock: medicineData.stock
    };

    return {
      code: 200,
      error: false,
      message: 'Medicine data retrieved',
      medicineData: orderedMedicineData,
    };
  } catch (error) {
    return {
      code: 500,
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

    // Reorder the user data properties
    const orderedUserData = {
      userId: userResult.userData.userId,
      diagnosisId: userResult.userData.diagnosisId,
      username: userResult.userData.username,
      email: userResult.userData.email,
      phone: userResult.userData.phone,
      address: userResult.userData.address,
      token: userResult.userData.token,
    };

    return res.status(200).json({
      code: 200,
      error: false,
      message: 'Medicine added to cart successfully',
      userData: orderedUserData,
      medicine: medicineResult.medicineData
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Failed to add medicine to cart',
    });
  }
});

module.exports = router;