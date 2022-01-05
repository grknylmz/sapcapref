using retailer as service from '../srv/retailer';

annotate service.Cepage with @Capabilities : {NavigationRestrictions : {
  $Type                : 'Capabilities.NavigationRestrictionsType',
  RestrictedProperties : [{
    $Type              : 'Capabilities.NavigationPropertyRestriction',
    InsertRestrictions : {
      $Type      : 'Capabilities.InsertRestrictionsType',
      Insertable : false
    },
    DeleteRestrictions : {
      $Type     : 'Capabilities.DeleteRestrictionsType',
      Deletable : false
    },
    NavigationProperty : to_vins,
  }, ],

}, };

annotate service.Vin with @Capabilities : {
  NavigationRestrictions : {
    $Type                : 'Capabilities.NavigationRestrictionsType',
    RestrictedProperties : [{
      $Type              : 'Capabilities.NavigationPropertyRestriction',
      InsertRestrictions : {
        $Type      : 'Capabilities.InsertRestrictionsType',
        Insertable : false
      },
      NavigationProperty : to_cepages,
    }, ],

  },
  FilterRestrictions     : {
    Filterable              : true,
    NonFilterableProperties : [
      'ID',
      'reference',
      'unit',
      'volume',
      'orderEnabled',
      'withdrawFromSaleEnabled',
      'postGoodsEnabled'
    ]
  }
}
actions {
  postGoods               @(
    Core.OperationAvailable             : in.postGoodsEnabled,
    Common.SideEffects.TargetProperties : [
      'in/availability_code',
      'in/postGoodsEnabled',
      'in/withdrawFromSaleEnabled',
      'in/orderEnabled',
      'in/inStockQty',
      'in/orderQty'
    ],
  );
  withdrawFromSale        @(
    Core.OperationAvailable             : in.withdrawFromSaleEnabled,
    Common.SideEffects.TargetProperties : [
      'in/availability_code',
      'in/postGoodsEnabled',
      'in/withdrawFromSaleEnabled',
      'in/orderEnabled',
      'in/inStockQty',
      'in/orderQty'
    ],
  );
  order                   @(
    Core.OperationAvailable             : in.orderEnabled,
    Common.SideEffects.TargetProperties : [
      'in/availability_code',
      'in/postGoodsEnabled',
      'in/withdrawFromSaleEnabled',
      'in/orderEnabled',
      'in/inStockQty',
      'in/orderQty'
    ],
  );
};


using customer as customer from '../srv/customer';

annotate customer.Cave with @(Capabilities : {FilterRestrictions : {
  Filterable              : true,
  NonFilterableProperties : [
    'vin_ID',
    'comment'
  ]
}}) actions {
  addQty                    @(Common.SideEffects.TargetProperties : ['in/quantity', ], );
  withdrawQty               @(Common.SideEffects.TargetProperties : ['in/quantity', ], );
  addRating                 @(Common.SideEffects.TargetProperties : ['in/rating', ], );
  addComment                @(Common.SideEffects.TargetProperties : ['in/comment', ], );
  addToStorage;
};

annotate customer.Vin with @(Capabilities : {FilterRestrictions : {
  Filterable              : true,
  NonFilterableProperties : [
    'volume',
    'unit',
    'inStockQty',
    'devise_code',
  ]
}});

annotate service.Vin actions {
  addGrapeVariety @(
    Core.OperationAvailable : in.IsActiveEntity,
    Common.SideEffects      : {
      $Type          : 'Common.SideEffectsType',
      TargetEntities : ['in/to_cepages'],

    }
  )
};

annotate service.Vin actions {
  addGrapeVariety(cepage_ID @(
    title  : '{i18n>cepage}',
    Common : {ValueListMapping : {
      Label          : '{i18n>Cepage}',
      CollectionPath : 'Cepage',
      Parameters     : [
        {
          $Type             : 'Common.ValueListParameterInOut',
          LocalDataProperty : cepage_ID,
          ValueListProperty : 'ID'
        },
        {
          $Type             : 'Common.ValueListParameterDisplayOnly',
          ValueListProperty : 'color'
        },
      ],
    }}
  ),
  pourcent                  @title : '{i18n>teneur}'
  )
};
