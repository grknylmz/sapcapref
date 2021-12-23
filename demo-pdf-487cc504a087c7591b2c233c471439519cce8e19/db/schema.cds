using {cuid, managed} from '@sap/cds/common';

namespace demo.pdf;

@Common : { 
    ChangedAt  : modifiedAt,
    ChangedBy  : modifiedBy,
    CreatedAt  : createdAt,
    CreatedBy  : createdBy,
    Heading  : 'Document Directory'
}
entity Directory: cuid, managed {
    fileName: String(50);
    fileDescription: String(200);
    pdf: Composition of one PDFDocument on pdf.directory = $self;
}

@Common : { 
    ChangedAt  : modifiedAt,
    ChangedBy  : modifiedBy,
    CreatedAt  : createdAt,
    CreatedBy  : createdBy,
    Heading    : 'PDF Document Repository'
}
entity PDFDocument: cuid, managed {
    mimeType: String(30) default 'application/pdf' @Core.IsMediaType;
    contents: LargeBinary @Core.MediaType: mimeType @Core.ContentDisposition.Filename: downloadName;
    downloadName: String(30) default 'PDFDocument.pdf';
    directory: Association to one Directory;
}