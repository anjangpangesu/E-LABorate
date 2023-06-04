const admin = require('firebase-admin');
const db = admin.firestore();

const verifyPrivateKey = async (key) => {
  try {
    const privateKeyDoc = await db.collection('privateKey').doc(key).get();
    
    return privateKeyDoc.exists;
  } catch (error) {
    throw new Error('Failed to verify private key');
  }
};

module.exports = verifyPrivateKey;
