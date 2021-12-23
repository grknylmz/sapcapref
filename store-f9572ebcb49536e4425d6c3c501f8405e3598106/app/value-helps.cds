using { epam.sap.dev.kitchen_app.store as my } from '../db/schema';
using {masterdata} from '../db/master-data';

//
// annotations for value helps
//

annotate my.Products {
phase @Common.ValueList: {
CollectionPath : 'Statuses',
Label : '',
Parameters : [
    { $Type: 'Common.ValueListParameterInOut', LocalDataProperty: phase_ID, ValueListProperty: 'ID'},
    { $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'name'}
],
SearchSupported : true
};
  measure @Common.ValueList: {
    CollectionPath : 'UOM',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: measure_msehi, ValueListProperty: 'msehi'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'name'}
    ],
    SearchSupported : true
  };

   toProductGroup @Common.ValueList: {
        CollectionPath : 'ProductGroups',
        Label : 'Product Groups',
        Parameters   : [
            { $Type : 'Common.ValueListParameterInOut', LocalDataProperty : 'toProductGroup_ID', ValueListProperty : 'ID'},
            { $Type : 'Common.ValueListParameterDisplayOnly', ValueListProperty : 'name'}                 
            ],
        SearchSupported : true
 };


CurrencyCode @Common.ValueList: {
    CollectionPath : 'Currencies',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: CurrencyCode_code, ValueListProperty: 'code'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'name'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'descr'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'symbol'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'minor'}
    ],
    SearchSupported : true
  };
}

annotate my.Markets {
    toMarketInfos @Common.ValueList: {
        CollectionPath : 'MarketMDs',
        Label : 'Market Info',
        Parameters   : [
            { $Type : 'Common.ValueListParameterInOut', LocalDataProperty : 'toMarketInfos_ID', ValueListProperty : 'ID'},
            { $Type : 'Common.ValueListParameterDisplayOnly', ValueListProperty : 'name'}                 
            ],
        SearchSupported : true
 };
}
