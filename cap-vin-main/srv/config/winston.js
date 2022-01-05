const { createLogger, format, transports } = require('winston');
const { combine, splat, timestamp, printf } = format;

const config = require('./config');

const myFormat = printf(({ level, message, timestamp, ...metadata }) => {
  let msg = `${timestamp} [${level}]: ${message} `;
  if (metadata) {
    msg += JSON.stringify(metadata);
  }
  return msg;
});

// define the custom settings for each transport (file, console)
const options = {
  console: {
    level: config.logLevel,
    colorize: true,
  },
};

// instantiate a new Winston Logger with the settings defined above
const logger = createLogger({
  format: combine(format.colorize(), splat(), timestamp(),myFormat),
  transports: [new transports.Console(options.console)],
  exitOnError: false, // do not exit on handled exceptions
});

// create a stream object with a 'write' function that will be used by `morgan`
logger.stream = {
  write: function (message) {
    // use the 'info' log level so the output will be picked up by both transports (file and console)
    logger.info(message);
  },
};

module.exports = logger;
