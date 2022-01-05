using retailer as service from '../../../srv/retailer';

annotate service.VinPerCepage with @UI : {
  LineItem         : [
    {
      $Type : 'UI.DataField',
      Value : name
    },
    {
      $Type  : 'UI.DataFieldForAnnotation',
      Target : '@UI.DataPoint#Count'
    }
  ],
  DataPoint #Count : {
    $Type : 'UI.DataPointType',
    Title : 'Count',
    Value : cepage_count
  }
};

annotate service.VinAnalytics with @UI : {
  LineItem                               : [
    {
      $Type : 'UI.DataField',
      Value : reference
    },

    {
      $Type : 'UI.DataField',
      Value : color
    },
    {
      $Type  : 'UI.DataFieldForAnnotation',
      Target : '@UI.DataPoint#Millesime'
    }
  ],
  DataPoint #Count                       : {
    $Type       : 'UI.DataPointType',
    Title       : 'Count',
    Value       : counter,
    ValueFormat : {$Type : 'UI.NumberFormat', },
  },
  DataPoint #Millesime                   : {
    $Type : 'UI.DataPointType',
    Title : 'Millesime',
    Value : millesime
  },
  Identification #VinPerCriticality      : [{
    $Type : 'UI.DataField',
    Value : status,
  },

  ],
  Identification #VinPerYear             : [{
    $Type : 'UI.DataField',
    Value : millesime,
  }, ],
  PresentationVariant #VinPerCriticality : {
    $Type          : 'UI.PresentationVariantType',
    GroupBy        : [status],
    Visualizations : ['@UI.Chart#VinPerCriticality', ],
  },
  PresentationVariant #VinPerColor       : {
    $Type          : 'UI.PresentationVariantType',
    GroupBy        : [color],
    Visualizations : ['@UI.Chart#VinPerCriticality', ],
  },
  PresentationVariant #VinPerYear        : {
    $Type          : 'UI.PresentationVariantType',
    GroupBy        : [millesime],
    Visualizations : ['@UI.Chart#VinPerYear', ],

  },
  Chart #VinPerColor                     : {
    $Type               : 'UI.ChartDefinitionType',
    ChartType           : #Donut,
    Title               : '{i18n>donutChartColorVin}',
    Dimensions          : ['color'],
    DimensionAttributes : [{
      $Type     : 'UI.ChartDimensionAttributeType',
      Dimension : 'color',
      Role      : #Category
    }],
    Measures            : ['counter'],
    MeasureAttributes   : [{
      $Type   : 'UI.ChartMeasureAttributeType',
      Measure : 'counter',
      Role    : #Axis1
    }]
  },
  Chart #VinPerYear                      : {
    $Type               : 'UI.ChartDefinitionType',
    ChartType           : #ColumnStacked,
    Title               : '{i18n>stackedColumnChartTypeYear}',
    Dimensions          : [
      color,
      millesime
    ],
    DimensionAttributes : [
      {
        $Type     : 'UI.ChartDimensionAttributeType',
        Dimension : color,
        Role      : #Series

      },
      {
        $Type     : 'UI.ChartDimensionAttributeType',
        Dimension : millesime,
        Role      : #Category
      }
    ],
    Measures            : [counter],
    MeasureAttributes   : [{
      $Type   : 'UI.ChartMeasureAttributeType',
      Measure : counter,
      Role    : #Axis1
    }]
  },
  Chart #VinPerCriticality               : {
    $Type               : 'UI.ChartDefinitionType',
    ChartType           : #Donut,
    Title               : '{i18n>donutChartStatusWine}',
    Dimensions          : [status],
    DimensionAttributes : [{
      $Type     : 'UI.ChartDimensionAttributeType',
      Dimension : status,
      Role      : #Category
    }],
    Measures            : [counter],
    MeasureAttributes   : [{
      $Type   : 'UI.ChartMeasureAttributeType',
      Measure : counter,
      Role    : #Axis1
    }]
  },
};

annotate service.SuperficieEvolution with @UI : {
  PresentationVariant #Surface_TimeSerie : {
    ID             : 'SuperficiePresVar',
    $Type          : 'UI.PresentationVariantType',
    Visualizations : ['@UI.Chart#Surface_TimeSerie'],
  },
  Identification #Surface_TimeSerie      : [{
    $Type : 'UI.DataField',
    Value : name,
  }, ],
  Chart #Surface_TimeSerie               : {
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
    Dimensions          : [
      annee,
      name
    ],
    DimensionAttributes : [
      {
        $Type     : 'UI.ChartDimensionAttributeType',
        Dimension : 'annee',
        Role      : #Category,
      },
      {
        $Type     : 'UI.ChartDimensionAttributeType',
        Dimension : 'name',
        Role      : #Series,
      }
    ],
  },
};
