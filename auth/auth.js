const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const crypto = require('crypto');

// Konfigurasi JWT
const JWT_SECRET = crypto.randomBytes(64).toString('hex');
const revokedTokens = new Set();

// Fungsi untuk membuat token JWT
const generateToken = (email) => {
  const token = jwt.sign({ email: email }, JWT_SECRET);
  revokedTokens.delete(token); // Remove token from revokedTokens if present
  return token;
};

// Fungsi untuk memverifikasi token JWT
const verifyToken = (token) => {
  return new Promise((resolve, reject) => {
    jwt.verify(token, JWT_SECRET, (err, decodedToken) => {
      if (err || revokedTokens.has(token)) {
        reject(err || 'Invalid token');
      } else {
        resolve(decodedToken);
      }
    });
  });
};

// Fungsi untuk memverifikasi password
const verifyPassword = async (password, hashedPassword) => {
  return bcrypt.compare(password, hashedPassword);
};

// Fungsi untuk mencatat token yang direvoke
const revokeToken = async (token) => {
  // Menghapus token dari database
  await deleteTokenFromDatabase(token);

  // Mencatat token yang direvoke
  revokedTokens.add(token);
};

// Fungsi untuk menghapus token dari database
const deleteTokenFromDatabase = async (token) => {
  try {
    const userQuery = db.collection('users').where('token', '==', token);
    const userSnapshot = await userQuery.get();

    if (!userSnapshot.empty) {
      const batch = db.batch();

      userSnapshot.forEach((doc) => {
        batch.update(doc.ref, { token: admin.firestore.FieldValue.delete() });
      });

      await batch.commit();
    }
  } catch (error) {
    // Tangani kesalahan jika terjadi
    return res.status(500).json({
      error: error,
      message: "Error deleting token"
    });
  }
};

// Fungsi untuk memeriksa apakah token telah direvoke
const isTokenRevoked = (token) => {
  return revokedTokens.has(token);
};

// Middleware untuk mengekstrak informasi pengguna dari token JWT
const extractUserFromToken = async (req, res, next) => {
  const token = req.header('Authorization');

  if (token) {
    try {
      const decodedToken = jwt.verify(token, JWT_SECRET);
      req.user = decodedToken;
    } catch (error) {
      // Jika token tidak valid, lakukan sign out paksa dan hapus token dari database
      await auth.revokeToken(token);
      return res.status(500).json({
        error: error,
        message: "Invalid token",
        tokenRevoked: isTokenRevoked
      });
    }
  }
};

module.exports = {
  JWT_SECRET,
  generateToken,
  verifyToken,
  verifyPassword,
  revokeToken,
  isTokenRevoked,
  extractUserFromToken
};
