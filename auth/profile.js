const express = require('express');
const admin = require('firebase-admin');
const userLog = require('../log/logger');

const router = express.Router();
const db = admin.firestore();

// Route untuk mendapatkan data profil pengguna
router.get('/:id', async (req, res) => {
  try {
    // Ambil data pengguna dari Firestore berdasarkan ID pengguna (sebagai contoh, menggunakan ID dari token JWT yang dikirim di header)
    const userId = req.params.id;
    const userDoc = await db.collection('users').doc(userId).get(); // userId ga bisa diakses langsung karena nama document ditulis berdasarkan email

    if (!userDoc.exists) {
      return res.status(404).json({
        error: true,
        message: 'User not found',
      });
    }

    const userData = userDoc.data();

    return res.status(200).json({
      error: false,
      message: 'User profile retrieved',
      userId: userData.id,
      email: userData.email,
      username: userData.username,
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
router.post('/', async (req, res) => {
  const { phone, address } = req.body;

  try {
    // Ambil ID pengguna dari token JWT (sebagai contoh, menggunakan ID dari token JWT yang dikirim di header)
    const userId = req.user.id; // Anda perlu mengatur middleware untuk mengekstrak informasi pengguna dari token JWT yang dikirim di header

    // Update dokumen pengguna dengan nomor telepon dan alamat rumah yang baru
    await db.collection('users').doc(userId).update({
      phone: phone,
      address: address,
    });

    // Mencatat pesan log menggunakan userLog
    userLog.info('User profile updated', { userId });

    return res.status(200).json({
      error: false,
      message: 'User profile updated successfully',
    });
  } catch (error) {
    return res.status(500).json({
      error: true,
      message: 'Error updating user profile',
    });
  }
});

module.exports = router;
