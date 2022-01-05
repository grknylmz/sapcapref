const winston = require('./config/winston');

module.exports = (srv) => {
  const { Vin, Cave, LogOfDemand, Position } = cds.entities('my.cave');

  const updateStatus = async () => {
    const vins = await SELECT.from(Vin).columns(['ID', 'status_code', 'annee', 'garde']);
    for (let vin of vins) {
      const status_code = calculateGardeCriticality(vin);
      await UPDATE(Vin, vin).with(`status_code = '${status_code}'`);
      vin.status_code = status_code;
    }
  };

  const calculateGardeCriticality = (item) => {
    const { annee, garde } = item;
    const currentYear = new Date().getFullYear();

    let status_code = 'E';
    if (annee && garde) {
      const ratio = (currentYear - annee) / garde;
      if (ratio < 0.5) {
        status_code = 'B';
      } else if (ratio > 0.5 && ratio < 0.9) {
        status_code = 'C';
      } else if (ratio >= 1) {
        status_code = 'D';
      }
    }
    return status_code;
  };
  //
  //
  srv.before('SAVE', 'Vin', async (req) => {
    winston.debug(['BEFORE', 'SAVE', 'Vin']);
    const tot = req.data.to_cepages.reduce((acc, { pourcent }) => {
      return acc + Number(pourcent);
    }, 0);
    if (tot > 100) req.reject('417', 'The composition of cepage is above 100% (actual:{0})', 'tot');
  });
  //
  //
  srv.on('SAVE', 'Vin', (req, next) => {
    winston.debug(['ON', 'SAVE', 'Vin']);
    return next();
  });
  //
  //
  srv.on('UPDATE', 'Vin', (req, next) => {
    winston.debug(['ON', 'UPDATE', 'Vin']);
    return next();
  });
  //
  //
  srv.on('READ', 'Vin', async (req, next) => {
    winston.debug(['ON', 'READ', 'Vin']);
    await updateStatus();
    return next();
  });
  //
  //
  srv.on('READ', 'VinAnalytics', async (req, next) => {
    winston.debug(['ON', 'READ', 'VinAnlytics']);
    await updateStatus();
    return next();
  });
  //
  //
  srv.after('READ', 'VinAnalytics', (lines) => {
    winston.debug(['AFTER', 'READ', 'VinAnalytics', JSON.stringify(lines)]);
  });
  //
  //
  //
  //
  srv.on('addToMyCellar', async (req) => {
    winston.debug(['ON', 'addToMyCellar']);
    const vin_ID = req.params[0];
    const { quantity } = req.data;
    const createdBy = req.user.id;
    const wine = await SELECT.one.from(Vin).columns(['ID', 'name', 'inStockQty']).where({ ID: vin_ID });
    const exists = await SELECT.one.from(Cave).columns(['ID', 'quantity']).where({ vin_ID, createdBy });
    let status;
    const modifiedBy = req.user.id;
    if (quantity > wine.inStockQty) {
      await INSERT.into(LogOfDemand).entries({ vin_ID, completed: false, quantity });
      req.error({
        code: '417',
        message: `Retailer does not possess the quantity requested. Actual: ${quantity}, Max available: ${wine.inStockQty}`,
        status: 417,
      });
    } else {
      if (exists) {
        await UPDATE(Cave, exists).with(`quantity = '${exists.quantity + quantity}'`);
        status = 200;
      } else {
        await INSERT({ quantity, vin_ID, createdBy, modifiedBy }).into(Cave);
        status = 201;
      }
      await UPDATE(Vin, vin_ID).with(
        `inStockQty  = ${wine.inStockQty - quantity}, availability_code = '${
          wine.inStockQty - quantity === 0 ? 'B' : 'C'
        }'`
      );
      await INSERT.into(LogOfDemand).entries({ vin_ID, completed: true, quantity });
      if (wine.inStockQty - quantity === 0) {
        const event = {
          reference: vin_ID,
        };
        srv.emit('productSoldOut', event);
      }
      req.notify({
        code: status.toString(),
        message: `${quantity} bottle(s) of ${wine.name} added to your wine cellar`,
        status,
      });
    }
  });
  //
  //
  srv.on('retailer.productAvailable', (req) => {
    req.notify({
      code: 200,
      message: `Message received`,
      status: 200,
    });
  });
  //
  //
  srv.on('addQty', async (req) => {
    winston.debug(['ON', 'addQty']);
    const ID = req.params[0];
    const { quantity } = req.data;
    const exists = await SELECT.one
      .from(Cave, (a) => {
        a.ID,
          a.quantity,
          a.vin((b) => {
            b.ID, b.name;
          });
      })
      .where({ ID });
    await UPDATE(Cave, exists.ID).with(`quantity = '${exists.quantity + quantity}'`);
    req.notify({
      code: '200',
      message: `${quantity} bottle(s) of ${exists.vin.name} added`,
      status: 200,
    });
  });
  //
  //
  srv.on('withdrawQty', async (req) => {
    const ID = req.params[0];
    const { quantity } = req.data;
    const exists = await SELECT.one
      .from(Cave, (a) => {
        a.ID,
          a.quantity,
          a.vin((b) => {
            b.name;
          });
      })
      .where({ ID });
    if (exists.quantity - quantity < 0) {
      req.error({
        code: '417',
        message: `You cannot withdraw more wine that you possess`,
        status: 417,
      });
    } else if (exists.quantity - quantity === 0) {
      await DELETE(Cave, exists.ID);
      req.warn({
        code: '202',
        message: `Following wine : ${exists.vin.name} will be removed from wine cellar`,
        status: 202,
      });
    } else {
      await UPDATE(Cave, exists.ID).with(`quantity = '${exists.quantity - quantity}'`);
      req.notify({
        code: '200',
        message: `${quantity} bottle(s) of ${exists.vin.name} withdrawn`,
        status: 200,
      });
    }
  });
  //
  //
  srv.on('addRating', async (req) => {
    winston.debug(['ON', 'addRating']);
    const ID = req.params[0];
    const { rating } = req.data;
    const exists = await SELECT.one
      .from(Cave, (a) => {
        a.ID;
      })
      .where({ ID });
    await UPDATE(Cave, exists.ID).with(`rating = ${rating}`);
    let message;
    if (rating <= 1) {
      message = 'Where you drunk when buying it ?';
    } else if (rating <= 3) {
      message = 'You may consider buying it again for a sangria';
    } else if (rating <= 4) {
      message = 'Good experience, worth buying it again';
    } else {
      message = 'Exceptionnal. Need more !!!';
    }
    req.notify({
      code: '200',
      message: `Your rating of ${rating} is saved. ${message}`,
      status: 200,
    });
  });
  //
  //
  srv.on('addComment', async (req) => {
    winston.debug(['ON', 'addComment']);
    const ID = req.params[0];
    const { comment } = req.data;
    const exists = await SELECT.one
      .from(Cave, (a) => {
        a.ID;
      })
      .where({ ID });
    await UPDATE(Cave, exists.ID).with(`comment  = '${comment}'`);
    req.notify({
      code: '201',
      message: `Your comment is saved`,
      status: 201,
    });
  });
  //
  //
  srv.on('addToStorage', async (req) => {
    winston.debug(['ON', 'addToStorage']);
    const ID = req.params[0];
    const { row: positionX, column: positionY } = req.data;
    const createdBy = req.user.id;
    const wine = await SELECT.one.from(Cave).where({ ID });
    const existingPositionsForWine = await SELECT(Position).where({ cave_ID: ID });
    const occupiedPosition = await SELECT.one.from(Position, (a) => {
      a.cave((b) => {
        b.vin((c) => {
          c.name,
            c.annee,
            c.color((d) => {
              d.name;
            });
        });
      });
    }).where({ createdBy, positionX, positionY });
    if (existingPositionsForWine.length >= wine.quantity) {
      req.error({
        code: '417',
        message: `You cannot store more wine that you possess`,
        status: 417,
      });
    } else if (occupiedPosition) {
      req.error({
        code: '417',
        message: `Position already occupied by ${occupiedPosition.cave.vin.name} - ${occupiedPosition.cave.vin.annee} - ${occupiedPosition.cave.vin.color.name}`,
        status: 417,
      });
    } else {
      await INSERT.into(Position).entries({ cave_ID: ID, positionX, positionY, createdBy });
    }
  });
};
