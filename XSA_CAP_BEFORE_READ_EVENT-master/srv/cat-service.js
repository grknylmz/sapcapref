const cds = require('@sap/cds');

class CatalogService extends cds.ApplicationService {
	init() {
		
		this.before('READ', 'SFLIGHT', (req) => {
			for (var i = 0; i < req.query.SELECT.where.length; i++) {
				if (req.query.SELECT.where[i].ref == 'CONNID') {
					req.query.SELECT.where[i + 2].val = req.query.SELECT.where[i + 2].val.padStart(4, '0');
				}
			}
		}
		
		)
		return super.init()
	}
}

module.exports = {
	CatalogService
}