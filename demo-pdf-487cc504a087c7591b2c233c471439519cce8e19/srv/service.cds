using {demo.pdf as db} from '../db/schema';

service DataService @(
    path: '/demo',
) {

    entity Directory as projection on db.Directory {
        *, pdf.ID as contentID, null as pdfUrl: String(255)
    }
    
    @cds.autoexpose
    entity Documents as projection on db.PDFDocument;

}