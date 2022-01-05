using {my.cave as schema} from './schema';

annotate schema.Vin with {
  name     @Common           : {
    ValueListWithFixedValues : false,
    ValueList                : {
      CollectionPath : 'Vin',
      Parameters     : [
        {
          $Type             : 'Common.ValueListParameterInOut',
          LocalDataProperty : 'name',
          ValueListProperty : 'name'
        },
        {
          $Type             : 'Common.ValueListParameterDisplayOnly',
          ValueListProperty : 'annee'
        },
        {
          $Type             : 'Common.ValueListParameterDisplayOnly',
          ValueListProperty : 'type'
        }
      ]
    }
  };

  type     @Common           : {
    ValueListWithFixedValues : false,
    ValueList                : {
      CollectionPath : 'TypeBoisson',
      Parameters     : [{
        $Type             : 'Common.ValueListParameterInOut',
        LocalDataProperty : 'type',
        ValueListProperty : 'ID'
      }]
    }
  };

  color    @Common           : {
    ValueListWithFixedValues : true,
    ValueList                : {
      CollectionPath : 'VinColor',
      Parameters     : [{
        $Type             : 'Common.ValueListParameterInOut',
        LocalDataProperty : 'color_code',
        ValueListProperty : 'code'
      }]
    }
  };

  currency @Common.ValueList : {
    CollectionPath  : 'Currencies',
    Label           : '',
    Parameters      : [
      {
        $Type             : 'Common.ValueListParameterInOut',
        LocalDataProperty : currency_code,
        ValueListProperty : 'code'
      },
      {
        $Type             : 'Common.ValueListParameterDisplayOnly',
        ValueListProperty : 'name'
      },
      {
        $Type             : 'Common.ValueListParameterDisplayOnly',
        ValueListProperty : 'descr'
      },
      {
        $Type             : 'Common.ValueListParameterDisplayOnly',
        ValueListProperty : 'symbol'
      }
    ],
    SearchSupported : true
  };
  region   @Common           : {
    ValueListWithFixedValues : false,
    ValueList                : {
      CollectionPath : 'Region',
      Parameters     : [
        {
          $Type             : 'Common.ValueListParameterInOut',
          LocalDataProperty : 'region_subregion',
          ValueListProperty : 'subregion',
        },
        {
          $Type             : 'Common.ValueListParameterDisplayOnly',
          ValueListProperty : 'region'
        },
        {
          $Type             : 'Common.ValueListParameterOut',
          ValueListProperty : 'country_code',
          LocalDataProperty : 'region/country_code',
        }
      ]
    }
  };
};

annotate schema.Assemblage with {
  cepage @Common : {ValueList : {
    $Type          : 'Common.ValueListType',
    CollectionPath : 'Cepage',
    Parameters     : [
      {
        $Type             : 'Common.ValueListParameterOut',
        LocalDataProperty : cepage_ID,
        ValueListProperty : 'ID',
      },
      {
        $Type             : 'Common.ValueListParameterDisplayOnly',
        ValueListProperty : 'description',
      },
      {
        $Type             : 'Common.ValueListParameterDisplayOnly',
        ValueListProperty : 'color',
      },

    ],
  }, }
};


annotate schema.Cepage with {
  description @UI.HiddenFilter;
  name        @Common : {
    ValueListWithFixedValues : false,
    ValueList                : {
      CollectionPath : 'Cepage',
      Parameters     : [
        {
          $Type             : 'Common.ValueListParameterInOut',
          LocalDataProperty : 'name',
          ValueListProperty : 'name',
        },
        {
          $Type             : 'Common.ValueListParameterDisplayOnly',
          ValueListProperty : 'color'
        }
      ]
    }
  };

  color       @(
    UI.HiddenFilter,
    Common : {
      ValueListWithFixedValues : true,
      ValueList                : {
        CollectionPath          : 'ColorCepage',
        DistinctValuesSupported : true,
        Parameters              : [{
          $Type             : 'Common.ValueListParameterInOut',
          LocalDataProperty : 'color',
          ValueListProperty : 'ID'
        }]
      }
    }
  );
};
