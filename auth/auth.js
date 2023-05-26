const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const crypto = require('crypto');

// JWT Configuration
const JWT_SECRET = crypto.randomBytes(64).toString('hex');
const revokedTokens = new Set();

// Function to create a JWT token
const generateToken = (email) => {
  const token = jwt.sign({ email: email }, JWT_SECRET);
  revokedTokens.delete(token); // Remove tokens from revokedTokens if found
  return token;
};

// Function to verify JWT token
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

// Function to verify password
const verifyPassword = async (password, hashedPassword) => {
  return bcrypt.compare(password, hashedPassword);
};

// Function to record deleted tokens
const revokeToken = async (token) => {
  // Menghapus token dari database
  await deleteTokenFromDatabase(token);

  // Record deleted tokens
  revokedTokens.add(token);
};

// Function to delete tokens from the database
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
    return res.status(500).json({
      error: error,
      message: "Error deleting token"
    });
  }
};

// Function to check if the token has been deleted
const isTokenRevoked = (token) => {
  return revokedTokens.has(token);
};

// Middleware to extract user information from JWT token
const extractUserFromToken = async (req, res) => {
  const token = req.header('Authorization');

  if (token) {
    try {
      const decodedToken = jwt.verify(token, JWT_SECRET);
      req.user = decodedToken;
    } catch (error) {
      // If the token is invalid, force sign out the user and remove the token from the database.
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
