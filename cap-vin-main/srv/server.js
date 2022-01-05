const cds = require('@sap/cds');
const cors = require('cors');
module.exports = cds.server;

async function getPlotChart(req, res, next) {
  const srv = await cds.connect.to('sqlite:db');
  const { Position } = srv.entities('my.cave');
  // const createdBy = req.user.id;
  const positions = await SELECT.from(Position, (a) => {
    a.positionX,
      a.positionY,
      a.cave((b) => {
        b.ID,
          b.vin((c) => {
            c.name;
            c.annee;
            c.color((d)=>{
              d.name
            });
          });
      });
  });
  res.send(positions);
}

cds.on('bootstrap', (app) => {
  if (process.env.NODE_ENV !== 'production') {
    const cds_swagger = require('cds-swagger-ui-express');
    app.use(cors());
    app.use(cds_swagger());
  }
  app.get('/getPlotChart', [getPlotChart]);
});
