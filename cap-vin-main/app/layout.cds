using retailer as retailer from '../srv/retailer';
using customer as customer from '../srv/customer';
/*-------------------
* Vin
*-------------------*/

annotate retailer.Vin with @(UI : {
  SelectionPresentationVariant #SPVPath : {
    $Type               : 'UI.SelectionPresentationVariantType',
    SelectionVariant    : {
      $Type : 'UI.SelectionVariantType',
      ID    : 'SV',
    },
    PresentationVariant : {
      $Type           : 'UI.PresentationVariantType',
      RequestAtLeast  : [
        reference,
        name,
        annee,
        color.name,
        igp,
        aoc,
        degre,
        volume,
        // garde,
        status.criticality,
        type,
        inStockQty,
        orderQty
      ],
      SelectionFields : [
        name,
        annee,
        color_code,
        type
      ],
      Visualizations  : ['@UI.LineItem', ],
      SortOrder       : [
        {
          $Type    : 'Common.SortOrderType',
          Property : name,
        },
        {
          $Type    : 'Common.SortOrderType',
          Property : annee,
        },
      ],
    },
  },
  HeaderInfo                            : {
    Title          : {
      $Type : 'UI.DataField',
      Value : name,
    },
    TypeName       : '{i18n>vin}',
    TypeNamePlural : '{i18n>vins}',
    Description    : {Value : annee},
    ImageUrl       : 'sap-icon://lab',
  },
  FilterFacets                          : [{
    Label  : '{i18n>label}',
    $Type  : 'UI.ReferenceFacet',
    Target : '@UI.FieldGroup#Labels',
  }, ],
  LineItem                              : [
    {
      Value             : reference,
      ![@UI.Importance] : #High,
      ![@UI.Emphasized] : true,
    },
    {
      Value             : name,
      ![@UI.Importance] : #High,
      ![@UI.Emphasized] : true,
    },
    {
      Value             : annee,
      ![@UI.Importance] : #High,
      ![@UI.Emphasized] : true
    },
    {Value : type},
    {
      Value             : color.name,
      Label             : '{i18n>color}',
      ![@UI.Importance] : #High,
      ![@UI.Emphasized] : true
    },
    {
      Value : region_subregion,
      Label : '{i18n>subregion}',
      ![@UI.Hidden]
    },
    {
      Value : region.region,
      Label : '{i18n>region}'
    },
    {
      Value : region.country.name,
      Label : '{i18n>country_name}'
    },
    {
      Value : inStockQty,
      Label : '{i18n>inStockQty}'
    },
    {
      Value : orderQty,
      Label : '{i18n>orderQty}'
    },
    // {Value : igp},
    // {Value : aoc},
    {Value : prix},
    // {Value : degre},
    // {Value : volume},
    {
      $Type                     : 'UI.DataField',
      Criticality               : availability.criticality,
      CriticalityRepresentation : #WithoutIcon,
      Label                     : '{i18n>availability}',
      Value                     : availability.name,
      ![@UI.Importance]         : #High
    },
    {
      Value : modifiedAt,
      ![@UI.Hidden]
    },
    {
      Value : createdAt,
      ![@UI.Hidden]
    },
    {
      Value : modifiedBy,
      ![@UI.Hidden]
    },
    {
      Value : createdBy,
      ![@UI.Hidden]
    },
    {
      Value : status_code,
      ![@UI.Hidden]
    },
    {
      Value : availability.criticality,
      ![@UI.Hidden]
    },
    {
      Value : availability_code,
      ![@UI.Hidden]
    },
    {
      Value : status.criticality,
      ![@UI.Hidden]
    },
    {
      Value : garde,
      ![@UI.Hidden]
    },
    {
      Value : color_code,
      ![@UI.Hidden]
    },
    {
      $Type  : 'UI.DataFieldForAction',
      Action : 'retailer.postGoods',
      Label  : '{i18n>postGoods}'
    },
    {
      $Type  : 'UI.DataFieldForAction',
      Action : 'retailer.withdrawFromSale',
      Label  : '{i18n>withdrawFromSale}'
    },
    {
      $Type  : 'UI.DataFieldForAction',
      Action : 'retailer.order',
      Label  : '{i18n>placeOrder}'
    },
    {
      Value : orderEnabled,
      ![@UI.Hidden]
    },
    {
      Value : postGoodsEnabled,
      ![@UI.Hidden]
    },
    {
      Value : withdrawFromSaleEnabled,
      ![@UI.Hidden]
    },
  ],
  SelectionFields                       : [
    name,
    annee,
    color_code,
    igp,
    aoc,
  ],
  HeaderFacets                          : [
    {
      $Type  : 'UI.ReferenceFacet',
      Target : '@UI.FieldGroup#Details',
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Target : '@UI.FieldGroup#Geography',
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Target : '@UI.FieldGroup#Conservation',
    },
  ],
  FieldGroup #Description               : {Data : [
    {
      $Type : 'UI.DataField',
      Value : name
    },
    {
      $Type : 'UI.DataField',
      Value : annee
    }
  ]},
  FieldGroup #Details                   : {Data : [
    {
      $Type : 'UI.DataField',
      Value : reference
    },
    {
      $Type : 'UI.DataField',
      Value : type
    },
    {
      $Type : 'UI.DataField',
      Value : color_code
    },
    {
      $Type : 'UI.DataField',
      Value : degre
    }
  ]},
  FieldGroup #Conservation              : {Data : [
    {
      $Type : 'UI.DataField',
      Value : garde
    },
    {
      $Type             : 'UI.DataField',
      Criticality       : status.criticality,
      Value             : status.name,
      ![@UI.Importance] : #High
    }
  ]},
  FieldGroup #Labels                    : {Data : [

    {
      $Type : 'UI.DataField',
      Value : igp
    },
    {
      $Type : 'UI.DataField',
      Value : aoc
    }
  ]},
  FieldGroup #Price                     : {Data : [

    {
      $Type : 'UI.DataField',
      Value : prix
    },
    {
      $Type : 'UI.DataField',
      Value : volume
    }
  ]},
  FieldGroup #DateData                  : {Data : [
    {
      $Type : 'UI.DataField',
      Value : createdBy
    },
    {
      $Type : 'UI.DataField',
      Value : modifiedBy
    },
    {
      $Type : 'UI.DataField',
      Value : createdAt
    },
    {
      $Type : 'UI.DataField',
      Value : modifiedAt
    }
  ]},

  FieldGroup #Geography                 : {
    $Type : 'UI.FieldGroupType',
    Data  : [
      {
        $Type : 'UI.DataField',
        Value : region_subregion,
      },
      {
        $Type : 'UI.DataField',
        Value : region.region,
      },
      {
        $Type                   : 'UI.DataField',
        Value                   : region.country_code,
        ![@Common.FieldControl] : #ReadOnly
      },
      {
        $Type                   : 'UI.DataField',
        Value                   : region.country.name,
        ![@Common.FieldControl] : #ReadOnly
      },
    ],
  },
  Facets                                : [
    {
      $Type  : 'UI.CollectionFacet',
      ID     : 'VinDetails',
      Label  : '{i18n>details}',
      Facets : [
        {
          $Type  : 'UI.ReferenceFacet',
          Label  : '{i18n>label}',
          Target : '@UI.FieldGroup#Labels'
        },
        {
          $Type  : 'UI.ReferenceFacet',
          Label  : '{i18n>prix}',
          Target : '@UI.FieldGroup#Price'
        },
        {
          $Type  : 'UI.ReferenceFacet',
          Label  : '{i18n>temporalData}',
          Target : '@UI.FieldGroup#DateData'
        }
      ],

    },
    {
      $Type  : 'UI.ReferenceFacet',
      Label  : '{i18n>assemblage}',
      Target : 'to_cepages/@UI.PresentationVariant#Vins'
    }
  ],
});

annotate retailer.Vin with @(UI.Identification : [
  {
    $Type : 'UI.DataField',
    Value : ID
  },
  {

    $Type              : 'UI.DataFieldForAction',
    Action             : 'retailer.addGrapeVariety',
    Inline             : false,
    InvocationGrouping : #Isolated,
    Determining        : false,
    Label              : 'Add grape variety',

  },
]) {
  ID     @UI.Hidden;
  unit   @UI.Hidden;
  devise @UI.Hidden;
};

annotate retailer.VinColor with {
  code @Common.Text : name  @Common.TextArrangement : #TextOnly
};

annotate retailer.RetentionStatus with {
  code @Common.Text : name  @Common.TextArrangement : #TextOnly
};
/*-------------------
* Assemblage
*-------------------*/

annotate retailer.Assemblage with @UI : {
  PresentationVariant #Cepages : {
    ID              : 'idCepagesPresVar',
    $Type           : 'UI.PresentationVariantType',
    Visualizations  : ['@UI.LineItem#Cepages'],
    SelectionFields : [
      vin.annee,
      vin.name
    ]
  },
  PresentationVariant #Vins    : {
    ID              : 'idVinsPresVar',
    $Type           : 'UI.PresentationVariantType',
    Visualizations  : ['@UI.LineItem#Vins'],
    SelectionFields : [cepage.name],
    SortOrder       : [{
      $Type    : 'Common.SortOrderType',
      Property : cepage.name,
    }, ],

  },
  LineItem #Cepages            : [
    {
      Value                   : vin.name,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.annee,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.type,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.igp,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.aoc,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.prix,
      ![@UI.Hidden],
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.degre,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.volume,
      ![@UI.Hidden],
      ![@Common.FieldControl] : #ReadOnly,
    }
  ],
  LineItem #Vins               : [
    {
      $Type                   : 'UI.DataField',
      Value                   : cepage.name,
      Label                   : '{i18n>cepage_name}',
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : cepage.description,
      Label                   : '{i18n>cepage_description}',
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : cepage.color,
      Label                   : '{i18n>color}',
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : pourcent,
      Label                   : '{i18n>teneur}',
      ![@Common.FieldControl] : #Optional,
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : vin_ID,
      Label                   : '{i18n>vin_ID}',
      ![@Common.FieldControl] : #ReadOnly,
      ![@UI.Hidden],
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : ID,
      Label                   : '{i18n>ID}',
      ![@Common.FieldControl] : #ReadOnly,
      ![@UI.Hidden],
    }
  ],
  FieldGroup #Description      : {Data : [
    {
      $Type : 'UI.DataField',
      Value : cepage.name
    },
    {
      $Type : 'UI.DataField',
      Value : cepage.description
    }
  ]},
  Facets                       : [{
    $Type  : 'UI.CollectionFacet',
    ID     : 'AssemblageDetails',
    Label  : '{i18n>details}',
    Facets : [{
      $Type  : 'UI.ReferenceFacet',
      Label  : '{i18n>label}',
      Target : '@UI.FieldGroup#Description'
    }]
  }],
};

/*-------------------
* Cepage
*-------------------*/

annotate retailer.Cepage with @UI : {
  SelectionVariant #Noir  : {
    $Type         : 'UI.SelectionVariantType',
    SelectOptions : [{
      $Type        : 'UI.SelectOptionType',
      PropertyName : color,
      Ranges       : [{
        $Type  : 'UI.SelectionRangeType',
        Sign   : #I,
        Option : #EQ,
        Low    : 'Noir',
      }]
    }],
    Text          : 'Noir'
  },
  SelectionVariant #Blanc : {
    $Type         : 'UI.SelectionVariantType',
    SelectOptions : [{
      $Type        : 'UI.SelectOptionType',
      PropertyName : color,
      Ranges       : [{
        $Type  : 'UI.SelectionRangeType',
        Sign   : #I,
        Option : #EQ,
        Low    : 'Blanc',
      }]
    }],
    Text          : 'Blanc'
  },
  HeaderInfo              : {
    Title          : {
      $Type : 'UI.DataField',
      Value : name,
    },
    TypeName       : '{i18n>cepage}',
    TypeNamePlural : '{i18n>cepages}',
    Description    : {Value : color},
    ImageUrl       : 'sap-icon://lab',
  },
  SelectionFields         : [name, ],
  LineItem                : [
    {
      $Type : 'UI.DataField',
      Value : name
    },
    {
      $Type : 'UI.DataField',
      Value : description
    }
  ],
  FieldGroup #Description : {Data : [
    {
      $Type : 'UI.DataField',
      Value : description
    },
    {
      $Type : 'UI.DataField',
      Value : color
    }
  ]},
  Facets                  : [
    {
      $Type  : 'UI.CollectionFacet',
      ID     : 'CepageDetails',
      Label  : '{i18n>details}',
      Facets : [{
        $Type  : 'UI.ReferenceFacet',
        Target : '@UI.FieldGroup#Description'
      },

      ],
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Label  : '{i18n>evolutionSuperficie}',
      ID     : 'SuperficieReferenceFacet',
      Target : 'to_superficies/@UI.PresentationVariant#to_superficies'
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Label  : '{i18n>Vins}',
      ID     : 'CepageVins',
      Target : 'to_vins/@UI.PresentationVariant#Cepages'
    }
  ]
};

/*-------------------
* Superficie
*-------------------*/

annotate retailer.Superficie with @UI : {
  HeaderInfo                          : {
    TypeName       : '{i18n>superficie}',
    TypeNamePlural : '{i18n>superficies}',
    ImageUrl       : 'sap-icon://lab',
  },
  PresentationVariant #to_superficies : {
    $Type          : 'UI.PresentationVariantType',
    SortOrder      : [{
      $Type      : 'Common.SortOrderType',
      Descending : true,
      Property   : annee,
    }, ],
    Visualizations : ['@UI.LineItem#to_superficies']
  },
  Chart #evolution                    : {
    $Type               : 'UI.ChartDefinitionType',
    ChartType           : #Line,
    Title               : 'Evolution',
    Description         : '',
    AxisScaling         : {$Type : 'UI.ChartAxisScalingType',

    },
    Measures            : [superficie],
    MeasureAttributes   : [{
      $Type   : 'UI.ChartMeasureAttributeType',
      Measure : 'superficie',
      Role    : #Axis1,
    }, ],
    Dimensions          : [annee],
    DimensionAttributes : [{
      $Type     : 'UI.ChartDimensionAttributeType',
      Dimension : 'annee',
      Role      : #Category,
    }, ],
  },
  DataPoint #superficie               : {
    $Type : 'UI.DataPointType',
    Value : 'superficie',
  },
  DataPoint #annee                    : {
    $Type : 'UI.DataPointType',
    Value : 'annee',
  },
  LineItem #to_superficies            : [
    {
      $Type : 'UI.DataField',
      Value : annee
    },
    {
      $Type : 'UI.DataField',
      Value : superficie
    }
  ],
};

annotate customer.Cave with @(UI : {
  Identification       : [
    {
      $Type : 'UI.DataField',
      Value : vin.name
    },
    {
      $Type : 'UI.DataField',
      Value : vin.annee
    },
    {
      $Type : 'UI.DataField',
      Value : vin.color.name
    },
    {
      $Type : 'UI.DataField',
      Value : vin.type
    },
    {
      $Type             : 'UI.DataFieldForAction',
      Label             : '{i18n>addQty}',
      Action            : 'customer.addQty',
      Determining       : true,
      ![@UI.Importance] : #High
    },
    {
      $Type              : 'UI.DataFieldForAction',
      Label              : '{i18n>addComment}',
      Action             : 'customer.addComment',
      InvocationGrouping : #Isolated,
      ![@UI.Importance]  : #High
    },
    {
      $Type              : 'UI.DataFieldForAction',
      Label              : '{i18n>addRating}',
      Action             : 'customer.addRating',
      InvocationGrouping : #Isolated,
      ![@UI.Importance]  : #High
    },
    {
      $Type              : 'UI.DataFieldForAction',
      Label              : '{i18n>addToStorage}',
      Action             : 'customer.addToStorage',
      InvocationGrouping : #Isolated,
      ![@UI.Importance]  : #High
    }
  ],
  PresentationVariant  : {
    $Type           : 'UI.PresentationVariantType',
    SelectionFields : [
      vin.name,
      vin.annee,
    ],
    SortOrder       : [
      {
        $Type    : 'Common.SortOrderType',
        Property : vin.name,
      },
      {
        $Type    : 'Common.SortOrderType',
        Property : vin.annee,
      },
    ],
  },
  SelectionFields      : [
    vin.name,
    vin.annee,
    rating,
  ],
  LineItem             : [
    {
      $Type : 'UI.DataField',
      Value : vin.name,
    },
    {
      $Type : 'UI.DataField',
      Value : vin.annee,
    },
    {
      $Type : 'UI.DataField',
      Value : vin.color_code,
    },
    {
      $Type : 'UI.DataField',
      Value : vin.type,
    },
    {
      $Type : 'UI.DataField',
      Value : quantity,
    },
    {
      $Type                     : 'UI.DataField',
      Value                     : vin.status.name,
      Criticality               : vin.status.criticality,
      CriticalityRepresentation : #OnlyIcon,
    },
    {
      $Type  : 'UI.DataFieldForAnnotation',
      Target : '@UI.DataPoint#rating'
    },
    {
      $Type  : 'UI.DataFieldForAction',
      Action : 'customer.addQty',
      Label  : '{i18n>addQty}'
    },
    {
      $Type  : 'UI.DataFieldForAction',
      Action : 'customer.withdrawQty',
      Label  : '{i18n>withdrawQty}'
    },
    {
      $Type  : 'UI.DataFieldForAction',
      Action : 'customer.addComment',
      Label  : '{i18n>addComment}'
    },
    {
      $Type  : 'UI.DataFieldForAction',
      Action : 'customer.addRating',
      Label  : '{i18n>addRating}'
    },
    {
      $Type : 'UI.DataField',
      Value : vin.inStockQty,
      ![@UI.Hidden]
    },
    {
      $Type : 'UI.DataField',
      Value : vin.unit,
      ![@UI.Hidden]
    },
    {
      $Type : 'UI.DataField',
      Value : vin_ID,
      ![@UI.Hidden]
    },
  ],
  DataPoint #rating    : {
    Value         : rating,
    Visualization : #Rating,
    TargetValue   : 5
  },
  HeaderInfo           : {
    $Type          : 'UI.HeaderInfoType',
    Title          : {
      $Type : 'UI.DataField',
      Value : vin.name,
    },
    ImageUrl       : 'sap-icon://blur',
    TypeName       : '{i18n>vin}',
    TypeNamePlural : '{i18n>vins}',
  },
  HeaderFacets         : [
    {
      $Type  : 'UI.ReferenceFacet',
      Target : '@UI.FieldGroup#Details',
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Target : '@UI.FieldGroup#Quantity',
    },
  ],
  FieldGroup #Details  : {
    $Type : 'UI.FieldGroupType',
    Data  : [
      {
        $Type : 'UI.DataField',
        Value : vin.name,
      },
      {
        $Type : 'UI.DataField',
        Value : vin.annee,
      },
      {
        $Type : 'UI.DataField',
        Value : vin.color.name,
        Label : '{i18n>color}'
      },
    ],
  },
  FieldGroup #Quantity : {
    $Type : 'UI.FieldGroupType',
    Data  : [
      {
        $Type : 'UI.DataField',
        Value : quantity,
      },
      {
        $Type                     : 'UI.DataField',
        Criticality               : vin.status.criticality,
        Value                     : vin.status.name,
        CriticalityRepresentation : #WithoutIcon,
      },

    ],
  },
  Facets               : [
    {
      $Type  : 'UI.ReferenceFacet',
      Target : 'vin/@UI.FieldGroup#Details',
      Label  : '{i18n>details}',
      ID     : '',
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Target : 'vin/@UI.FieldGroup#Geography',
      Label  : '{i18n>geography}',
      ID     : '',
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Label  : '{i18n>assemblage}',
      Target : 'vin/to_cepages/@UI.PresentationVariant#Vins'
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Label  : '{i18n>location}',
      Target : 'to_positions/@UI.LineItem#Storage'
    }
  ],
});

annotate customer.Assemblage with @UI : {
  PresentationVariant #Cepages : {
    ID              : 'idCepagesPresVar',
    $Type           : 'UI.PresentationVariantType',
    Visualizations  : ['@UI.LineItem#Cepages'],
    SelectionFields : [
      vin.annee,
      vin.name
    ]
  },
  PresentationVariant #Vins    : {
    ID              : 'idVinsPresVar',
    $Type           : 'UI.PresentationVariantType',
    Visualizations  : ['@UI.LineItem#Vins'],
    SelectionFields : [cepage.name],
    SortOrder       : [{
      $Type    : 'Common.SortOrderType',
      Property : cepage.name,
    }, ],

  },
  LineItem #Cepages            : [
    {
      Value                   : vin.name,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.annee,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.type,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.igp,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.aoc,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.prix,
      ![@UI.Hidden],
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.degre,
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      Value                   : vin.volume,
      ![@UI.Hidden],
      ![@Common.FieldControl] : #ReadOnly,
    }
  ],
  LineItem #Vins               : [
    {
      $Type : 'UI.DataField',
      Value : cepage.name,
      Label : '{i18n>cepage}',
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : cepage.description,
      Label                   : '{i18n>cepage_description}',
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : cepage.color,
      Label                   : '{i18n>color}',
      ![@Common.FieldControl] : #ReadOnly,
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : pourcent,
      Label                   : '{i18n>teneur}',
      ![@Common.FieldControl] : #Optional,
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : vin_ID,
      Label                   : '{i18n>vin_ID}',
      ![@Common.FieldControl] : #ReadOnly,
      ![@UI.Hidden],
    },
    {
      $Type                   : 'UI.DataField',
      Value                   : ID,
      Label                   : '{i18n>ID}',
      ![@Common.FieldControl] : #ReadOnly,
      ![@UI.Hidden],
    }
  ],
  FieldGroup #Description      : {Data : [
    {
      $Type : 'UI.DataField',
      Value : cepage.name
    },
    {
      $Type : 'UI.DataField',
      Value : cepage.description
    }
  ]},
  Facets                       : [{
    $Type  : 'UI.CollectionFacet',
    ID     : 'AssemblageDetails',
    Label  : '{i18n>details}',
    Facets : [{
      $Type  : 'UI.ReferenceFacet',
      Label  : '{i18n>label}',
      Target : '@UI.FieldGroup#Description'
    }]
  }],
};


annotate customer.Vin with @(UI : {
  SelectionPresentationVariant #SPVPath : {
    $Type               : 'UI.SelectionPresentationVariantType',
    SelectionVariant    : {
      $Type : 'UI.SelectionVariantType',
      ID    : 'SV',
    },
    PresentationVariant : {
      $Type           : 'UI.PresentationVariantType',
      RequestAtLeast  : [
        name,
        annee,
        color.name,
        igp,
        aoc,
        degre,
        volume,
        garde,
        status.criticality,
        type,
      ],
      SelectionFields : [
        name,
        annee,
        color_code,
        type
      ],
      Visualizations  : ['@UI.LineItem', ],
      SortOrder       : [
        {
          $Type    : 'Common.SortOrderType',
          Property : name,
        },
        {
          $Type    : 'Common.SortOrderType',
          Property : annee,
        },
      ],
    },
  },
  HeaderInfo                            : {
    Title          : {
      $Type : 'UI.DataField',
      Value : name,
    },
    TypeName       : '{i18n>vin}',
    TypeNamePlural : '{i18n>vins}',
    Description    : {Value : annee},
    ImageUrl       : 'sap-icon://lab',
  },
  FilterFacets                          : [{
    Label  : '{i18n>label}',
    $Type  : 'UI.ReferenceFacet',
    Target : '@UI.FieldGroup#Labels',
  }, ],
  LineItem                              : [
    {
      Value             : name,
      ![@UI.Importance] : #High,
      ![@UI.Emphasized] : true,
    },
    {
      Value             : annee,
      ![@UI.Importance] : #High,
      ![@UI.Emphasized] : true
    },
    {Value : type},
    {
      Value             : color.name,
      Label             : '{i18n>color}',
      ![@UI.Importance] : #High,
      ![@UI.Emphasized] : true
    },
    {
      Value : region_subregion,
      Label : '{i18n>subregion}',
      ![@UI.Hidden]
    },
    {
      Value : region.region,
      Label : '{i18n>region}'
    },
    {
      Value : region.country.name,
      Label : '{i18n>country_name}'
    },
    {Value : igp},
    {Value : aoc},
    {Value : prix},
    {Value : degre},
    {Value : volume},
    {
      $Type             : 'UI.DataField',
      Criticality       : status.criticality,
      Label             : '{i18n>retentionstatus}',
      Value             : status.name,
      ![@UI.Importance] : #High
    },
    {
      $Type  : 'UI.DataFieldForAction',
      Action : 'customer.addToMyCellar',
      Label  : '{i18n>addToMyCellar}'
    },
    {
      Value : status_code,
      ![@UI.Hidden]
    },
    {
      Value : status.criticality,
      ![@UI.Hidden]
    },
    {
      Value : garde,
      ![@UI.Hidden]
    },
    {
      Value : color_code,
      ![@UI.Hidden]
    },
    {
      $Type : 'UI.DataField',
      Value : inStockQty,
      ![@UI.Hidden]
    },
    {
      $Type : 'UI.DataField',
      Value : unit,
      ![@UI.Hidden]
    },
    {
      $Type : 'UI.DataField',
      Value : devise_code,
      ![@UI.Hidden]
    }
  ],
  SelectionFields                       : [
    name,
    annee,
    color_code,
    igp,
    aoc,
  ],
  HeaderFacets                          : [
    {
      $Type  : 'UI.ReferenceFacet',
      Target : '@UI.FieldGroup#Details',
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Target : '@UI.FieldGroup#Geography',
    },
    {
      $Type  : 'UI.ReferenceFacet',
      Target : '@UI.FieldGroup#Conservation',
    },
  ],
  FieldGroup #Description               : {Data : [
    {
      $Type : 'UI.DataField',
      Value : name
    },
    {
      $Type : 'UI.DataField',
      Value : annee
    }
  ]},
  FieldGroup #Details                   : {Data : [
    {
      $Type : 'UI.DataField',
      Value : type
    },
    {
      $Type : 'UI.DataField',
      Value : color_code
    },
    {
      $Type : 'UI.DataField',
      Value : degre
    }
  ]},
  FieldGroup #Conservation              : {Data : [
    {
      $Type : 'UI.DataField',
      Value : garde
    },
    {
      $Type             : 'UI.DataField',
      Criticality       : status.criticality,
      Value             : status.name,
      ![@UI.Importance] : #High
    }
  ]},
  FieldGroup #Labels                    : {Data : [

    {
      $Type : 'UI.DataField',
      Value : igp
    },
    {
      $Type : 'UI.DataField',
      Value : aoc
    }
  ]},
  FieldGroup #Price                     : {Data : [

    {
      $Type : 'UI.DataField',
      Value : prix
    },
    {
      $Type : 'UI.DataField',
      Value : volume
    }
  ]},
  FieldGroup #Geography                 : {
    $Type : 'UI.FieldGroupType',
    Data  : [
      {
        $Type : 'UI.DataField',
        Value : region_subregion,
      },
      {
        $Type : 'UI.DataField',
        Value : region.region,
      },
      {
        $Type                   : 'UI.DataField',
        Value                   : region.country_code,
        ![@Common.FieldControl] : #ReadOnly
      },
      {
        $Type                   : 'UI.DataField',
        Value                   : region.country.name,
        ![@Common.FieldControl] : #ReadOnly
      },
    ],
  },
  Facets                                : [
    {
      $Type  : 'UI.CollectionFacet',
      ID     : 'VinDetails',
      Label  : '{i18n>details}',
      Facets : [
        {
          $Type  : 'UI.ReferenceFacet',
          Label  : '{i18n>label}',
          Target : '@UI.FieldGroup#Labels'
        },
        {
          $Type  : 'UI.ReferenceFacet',
          Label  : '{i18n>prix}',
          Target : '@UI.FieldGroup#Price'
        }
      ],

    },
    {
      $Type  : 'UI.ReferenceFacet',
      Label  : '{i18n>assemblage}',
      Target : 'to_cepages/@UI.PresentationVariant#Vins'
    }
  ],
});

annotate customer.Position with @UI : {LineItem #Storage : [
  {
    $Type : 'UI.DataField',
    Value : positionY,
  },
  {
    $Type : 'UI.DataField',
    Value : positionX,
  },
], };


annotate customer.Cave with @(Common : {SemanticKey : [
  vin.name,
  vin.annee
]});

annotate retailer.Vin with @(Common : {SemanticKey : [
  ID,
  reference,
]});

annotate customer.Vin with @(Common : {SemanticKey : [
  ID,
  annee,
  color_code,
  name
]});

annotate retailer.Cepage with @(Common : {SemanticKey : [name], }, );