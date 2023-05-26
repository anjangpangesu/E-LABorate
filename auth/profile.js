const express = require('express');
const admin = require('firebase-admin');
const userLog = require('../log/logger');

const router = express.Router();
const db = admin.firestore();

// Route untuk mendapatkan data profil pengguna
router.get('/:id', async (req, res) => {
  try {
    const userId = req.params.id;

    // Ambil email pengguna berdasarkan ID pengguna
    const userQuery = await db.collection('users').where('id', '==', userId).get();

    if (userQuery.empty) {
      return res.status(404).json({
        error: true,
        message: 'User not found',
      });
    }

    const userDoc = userQuery.docs[0]; // Mengambil document pertama yang ditemukan
    const userEmail = userDoc.data().email;

    // Ambil data pengguna dari Firestore berdasarkan email
    const userDataQuery = await db.collection('users').where('email', '==', userEmail).get();

    if (userDataQuery.empty) {
      return res.status(404).json({
        error: true,
        message: 'User not found',
      });
    }

    const userDataDoc = userDataQuery.docs[0]; // Mengambil document pertama yang ditemukan
    const userData = userDataDoc.data();

    return res.status(200).json({
      error: false,
      message: 'User profile retrieved',
      userId: userData.id,
      username: userData.username,
      email: userData.email,
      token: userData.token,
      phone: userData.phone,
      address: userData.address,
    });
  } catch (error) {
    return res.status(500).json({
      error: true,
      message: 'Error retrieving user profile',
    });
  }
});

// Route untuk menambahkan nomor telepon dan alamat rumah pengguna
router.post('/:id', async (req, res) => {
  const { phone, address } = req.body;

  try {
    const userId = req.params.id;

    // Ambil email pengguna berdasarkan ID pengguna
    const userQuery = await db.collection('users').where('id', '==', userId).get();

    if (userQuery.empty) {
      return res.status(404).json({
        error: true,
        message: 'User not found',
      });
    }

    const userDoc = userQuery.docs[0]; // Mengambil document pertama yang ditemukan
    const userEmail = userDoc.data().email;

    // Update dokumen pengguna dengan nomor telepon dan alamat rumah yang baru
    await db.collection('users').doc(userEmail).update({
      phone: phone,
      address: address,
    });

    // Mencatat pesan log menggunakan userLog
    userLog.info('User profile updated', { email: userEmail });

    return res.status(200).json({
      error: false,
      message: 'User profile updated successfully',
    });
  } catch (error) {
    return res.status(500).json({
      error: error,
      message: 'Error updating user profile',
    });
  }
});

module.exports = router;
