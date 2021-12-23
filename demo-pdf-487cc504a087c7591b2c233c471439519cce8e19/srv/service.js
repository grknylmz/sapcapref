const cds = require('@sap/cds');

class DataService extends cds.ApplicationService {
    async init() {
        this.after('READ', 'Directory', (each) => {
            each.pdfUrl = `/demo/Documents(${each.contentID})/contents`;
        });
        await super.init();
    }
}

module.exports = DataService;