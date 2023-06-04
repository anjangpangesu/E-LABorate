const winston = require('winston');
const { format } = winston;
const { timestamp } = format;

// Create a new format for user logs
const userFormat = format.printf(info => {
  const customTimestamp = info.timestamp;
  const level = info.level.toUpperCase();
  const userId = info.userId;
  const username = info.username;
  const email = info.email;
  const phone = info.phone;
  const address = info.address;
  const message = info.message;

  return `${customTimestamp} - ${level} - ${message} - ${userId} - ${username} - ${email} - ${phone} - ${address}`;
});

// Creating userLog
const userLog = winston.createLogger({
  format: format.combine(
    timestamp({
      format: 'DD-MM-YYYY HH:mm:ss'
    }),
    userFormat
  ),
  transports: [
    new winston.transports.File({ filename: './log/user.log' })
  ]
});

module.exports = userLog;
