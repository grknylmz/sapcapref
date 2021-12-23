using { sap.fe.cap.travel as my } from '../../db/schema';

//
// annotations for value helps
//

annotate my.Box {

  BoxStatus @Common.ValueListWithFixedValues;

  to_Customer @Common.ValueList: {
    CollectionPath : 'Passenger',
    Label : 'Customer ID',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: to_Customer_CustomerID, ValueListProperty: 'CustomerID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'FirstName'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'LastName'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Title'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Street'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'PostalCode'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'City'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'CountryCode_code'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'PhoneNumber'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'EMailAddress'}
    ],
    SearchSupported : true
  };
}


annotate my.Geraete {

  GeraeteStatus @Common.ValueListWithFixedValues;

  to_Customer @Common.ValueList: {
    CollectionPath : 'Passenger',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: to_Customer_CustomerID, ValueListProperty: 'CustomerID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'FirstName'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'LastName'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Title'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Street'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'PostalCode'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'City'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'CountryCode_code'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'PhoneNumber'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'EMailAddress'}
    ],
    SearchSupported : true
  };

  to_Carrier @Common.ValueList: {
    CollectionPath : 'Airline',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: to_Carrier_AirlineID, ValueListProperty: 'AirlineID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Name'},
    ],
    SearchSupported : true
  };

  ConnectionID @Common.ValueList: {
    CollectionPath : 'Flight',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: to_Carrier_AirlineID,    ValueListProperty: 'AirlineID'},
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: ConnectionID, ValueListProperty: 'ConnectionID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'to_Airline/Name'},
    ],
    SearchSupported : true,
    PresentationVariantQualifier: 'SortOrderPV'  // use presentation variant to sort by FlightDate desc
  };
}





annotate my.Flight {

  AirlineID @Common.ValueList: {
    CollectionPath : 'Airline',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: AirlineID, ValueListProperty: 'AirlineID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Name'},
    ],
    SearchSupported : true
  };
}

annotate my.Passenger {

  CountryCode @Common.ValueList : {
    CollectionPath  : 'Countries',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut',       LocalDataProperty : CountryCode_code, ValueListProperty : 'code'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty : 'name'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty : 'descr'}
    ],
    SearchSupported : true
  };

}



