using BoxService from '../../srv/travel-service';

//
// annotatios that control the fiori layout
//

annotate BoxService.Box with @UI : {

  Identification : [
    { $Type  : 'UI.DataFieldForAction', Action : 'BoxService.acceptBox',   Label  : '{i18n>AcceptBox}'   },
    { $Type  : 'UI.DataFieldForAction', Action : 'BoxService.rejectBox',   Label  : '{i18n>RejectBox}'   }
    //{ $Type  : 'UI.DataFieldForAction', Action : 'BoxService.deductDiscount', Label  : '{i18n>DeductDiscount}' }
  ],
  HeaderInfo : {
    TypeName       : '{i18n>Box}',
    TypeNamePlural : '{i18n>Boxs}',
    Title          : {
      $Type : 'UI.DataField',
      Value : BoxID
    },
    Description    : {
      $Type : 'UI.DataField',
      Value : '{i18n>BoxID}'
    }
  },
  PresentationVariant : {
    Text           : 'Default',
    Visualizations : ['@UI.LineItem'],
    SortOrder      : [{
      $Type      : 'Common.SortOrderType',
      Property   : BoxID,
      Descending : true
    }]
  },
  SelectionFields : [
    BoxID,
    to_Customer_CustomerID,
    BoxStatus_code
  ],
  LineItem : [
    { $Type  : 'UI.DataFieldForAction', Action : 'BoxService.acceptBox',   Label  : '{i18n>AcceptBox}'   },
    { $Type  : 'UI.DataFieldForAction', Action : 'BoxService.rejectBox',   Label  : '{i18n>RejectBox}'   },
    //{ $Type  : 'UI.DataFieldForAction', Action : 'BoxService.deductDiscount', Label  : '{i18n>DeductDiscount}' },
    { Value : BoxID               },
    { Value : to_Customer_CustomerID },
    { Value : BeginDateAusleihe              },
    { Value : EndDateAusleihe                },
    { Value : Boxname            },
    { $Type : 'UI.DataField', Value : BoxStatus_code, Criticality : BoxStatus.criticality }
  ],
  Facets : [{
    $Type  : 'UI.CollectionFacet',
    Label  : '{i18n>Box}',
    ID     : 'Box',
    Facets : [
      {  // travel details
        $Type  : 'UI.ReferenceFacet',
        ID     : 'BoxData',
        Target : '@UI.FieldGroup#BoxData',
        Label  : '{i18n>Box}'
      },
      {  // date information
        $Type  : 'UI.ReferenceFacet',
        ID     : 'DateData',
        Target : '@UI.FieldGroup#DateData',
        Label  : '{i18n>Dates}'
      }
      ]
  }, {  // booking list
    $Type  : 'UI.ReferenceFacet',
    Target : 'to_Geraete/@UI.PresentationVariant',
    Label  : '{i18n>Geraete}'
  }],
  FieldGroup#BoxData : { Data : [
    { Value : BoxID               },
    { Value : to_Customer_CustomerID },
    { Value : Boxname            },
    {
      $Type       : 'UI.DataField',
      Value       : BoxStatus_code,
      Criticality : BoxStatus.criticality,
      Label : '{i18n>Status}' // label only necessary if differs from title of element
    }
  ]},
  FieldGroup #DateData : {Data : [
    { $Type : 'UI.DataField', Value : BeginDateAusleihe },
    { $Type : 'UI.DataField', Value : EndDateAusleihe }
  ]}
};

annotate BoxService.Geraete with @UI : {
  Identification : [
    { Value : GeraeteID },
  ],
  HeaderInfo : {
    TypeName       : '{i18n>Geraete}',
    TypeNamePlural : '{i18n>Geraete}',
    Title          : { Value : GeraeteID },
    Description    : {
      $Type : 'UI.DataField',
      Value : '{i18n>GeraeteID}'
    }
  },
  PresentationVariant : {
    Visualizations : ['@UI.LineItem'],
    SortOrder      : [{
      $Type      : 'Common.SortOrderType',
      Property   : GeraeteID,
      Descending : false
    }]
  },
  SelectionFields : [],
  LineItem : [
    { Value : to_Carrier.AirlinePicURL,  Label : '  '},
    { Value : GeraeteID,             Label : '{i18n>BookingNumber}' },
    { Value : to_Carrier_AirlineID   },
   // { Value : ConnectionID,          Label : '{i18n>FlightNumber}' },
    { Value : GeraeteStatus_code     }
  ],
  Facets : [{
    $Type  : 'UI.CollectionFacet',
    Label  : '{i18n>Geraete}',
    ID     : 'Geraete',
    Facets : [{  // booking details
      $Type  : 'UI.ReferenceFacet',
      ID     : 'GeraeteData',
      Target : '@UI.FieldGroup#GeraeteData',
      Label  : 'Geraete'
    }]
  }, ],
  FieldGroup #GeraeteData : { Data : [
    { Value : GeraeteID              },
    { Value : to_Customer_CustomerID },
    { Value : to_Carrier_AirlineID   },
    { Value : ConnectionID           },
    { Value : GeraeteStatus_code     }
  ]},
};




