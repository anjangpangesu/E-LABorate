const winston = require('winston');
const { format } = winston;
const { timestamp } = format;

// Create a new format for user logs
const userFormat = format.printf(info => {
  const customTimestamp = info.timestamp;
  const level = info.level.toUpperCase();
  const email = info.email;
  const message = info.message;

  return `${customTimestamp} - ${level} - ${email} - ${message}`;
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
