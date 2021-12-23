using DataService as service from '../../srv/service';

annotate service.Directory with @(
    UI.HeaderInfo                   : {
        $Type          : 'UI.HeaderInfoType',
        TypeName       : 'Document',
        TypeNamePlural : 'Document List',
        Title          : {
            $Type : 'UI.DataField',
            Value : ID,
        },
        Description    : {
            $Type : 'UI.DataField',
            Value : fileDescription,
        },
        ImageUrl       : 'sap-icon://document'
    },
    UI.SelectionFields              : [
        fileName,
    ],
    UI.LineItem                     : [
        {
            $Type : 'UI.DataField',
            Value : ID,
            Label : 'Document ID'
        },
        {
            $Type : 'UI.DataField',
            Value : fileDescription,
            Label : 'Document Description'
        },
        {
            $Type : 'UI.DataField',
            Value : createdAt,
        },
        {
            $Type : 'UI.DataField',
            Value : createdBy,
        },
        {
            $Type : 'UI.DataField',
            Value : modifiedAt
        },
        {
            $Type : 'UI.DataField',
            Value : modifiedBy,
        }
    ],
    UI.FieldGroup #DocumentData: {
        Data : [
            {
                $Type : 'UI.DataField',
                Value : fileName,
            },
            {
                $Type : 'UI.DataField',
                Value : fileDescription,
            },
        ],
    },
    UI.FieldGroup #AdminData        : {Data : [
        {
            $Type : 'UI.DataField',
            Value : createdBy,
        },
        {
            $Type : 'UI.DataField',
            Value : createdAt,
        },
        {
            $Type : 'UI.DataField',
            Value : contentID,
            Label : 'Contents ID'
        },
        {
            $Type : 'UI.DataFieldWithUrl',
            Url : pdfUrl,
            UrlContentType : pdf.mimeType,
            Value : 'Download Contents',
        },        
    ], },
    UI.HeaderFacets                 : [{
        $Type  : 'UI.ReferenceFacet',
        Target : '@UI.FieldGroup#AdminData',
    }, ],
    UI.Facets                       : [{
        $Type  : 'UI.CollectionFacet',
        ID: 'DocumentHeaderData',
        Label: 'Document Header',
        Facets : [{
            $Type  : 'UI.ReferenceFacet',
            Target : '@UI.FieldGroup#DocumentData',
            Label  : 'Document Info'
        }, ],
    }, ],
) {
    ID            @Core.Computed @Common : {
        Text            : fileName,
        TextArrangement : #TextFirst,
    };
    fileDescription @UI.MultiLineText: true
}