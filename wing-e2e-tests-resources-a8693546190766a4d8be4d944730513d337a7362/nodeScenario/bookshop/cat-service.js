const cds = require('@sap/cds');
module.exports = async (srv) => {

    // Using CDS API
    const businessPartnerServiceReal = await cds.connect.to("metadata");
    srv.on('READ', 'BusinessPartnersReal', req => businessPartnerServiceReal.tx(req).run(req.query));

}
