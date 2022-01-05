const cds = require('@sap/cds');
const winston = require('./config/winston')

module.exports = async (srv) => {
  const retailerService = await cds.connect.to('retailer');
  const customerService = await cds.connect.to('customer');
  //
  //
  retailerService.on('productAvailable', (msg) => {
    winston.info(['==>> msg received from customerService', msg.event, msg.data]);
  });
  //
  //
  retailerService.on('productOrdered', (msg) => {
    winston.info(['==>> msg received from customerService', msg.event, msg.data]);
  });
  //
  //
  customerService.on('productSoldOut', (msg) => {
    winston.info(['==>> msg received from retailerService', msg.event, msg.data]);
  });
};
