# Strange behavior of CAP with PDF files

Clone the project, then ```npm install``` then ```cds deploy --to sqlite```

Use file [api.http](api.http) to create initial entities:

POST http://localhost:4004/demo/Directory with

```
{ "fileName": "Just a file", "fileDescription": "Just a description" }
```

Take the generated ID, then create a media resource:

POST http://localhost:4004/demo/Documents with

```
{ "mimeType": "application/pdf", "directory_ID": "<generated above>", "downloadName": "Test_PDF.pdf" }
```

MimeType is used by ```@Core.MediaType``` annotation.

DownloadName is used by ```@Core.ContentDisposition.Filename``` annotation.

Launch the LROP page and within the object page see the "not viewable" message from sap.m.PDFViewer.
Try to download the PDF via the link in the Object Page header and see the "empty" PDF.

Test via Postman:

http://localhost:4004/demo/Documents('--your--ID--here')/contents 

returns a perfectly viewable PDF.

## Browser Testing

Browser | Display in sap.m.PDFViewer | Download
--------|----------------------------|---------
Chrome 92.0.4515.131 | Fails with "The PDF file could not be loaded" message | Empty PDF
Edge 92.0.902.73 | Automatically downloads the PDF file and sap.m.PDFViewer "hangs" on busy animation | Empty PDF
Safari 14.1.2 | as Edge | Empty PDF
Firefox 89.0 | Automatically downloads the PDF file and sap.m.PDFViewer shows "The PDF file could not be loaded" message | Empty PDF
