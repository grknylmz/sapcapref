using { slah.db as my } from '../../db/schema';

//
// annotations for value helps
//

annotate my.Box {

  BoxStatus @Common.ValueListWithFixedValues;

  to_Patient @Common.ValueList: {
    CollectionPath : 'Patient',
    Label : 'Patient ID',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: to_Patient_PatientID, ValueListProperty: 'PatientID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Vorname'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Nachname'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Anrede'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Strasse'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Plz'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Stadt'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'CountryCode_code'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Telefonnr'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'EMail'}
    ],
    SearchSupported : true
  };

}


annotate my.Geraete {

  GeraeteStatus @Common.ValueListWithFixedValues;

  to_Patient @Common.ValueList: {
    CollectionPath : 'Patient',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: to_Patient_PatientID, ValueListProperty: 'PatientID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Vorname'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Nachname'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Anrede'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Strasse'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Plz'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Stadt'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'CountryCode_code'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Telefonnr'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'EMail'}
    ],
    SearchSupported : true
  };
  
  to_Geraetetyp @Common.ValueList: {
    CollectionPath : 'Geraetetyp',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: to_Geraetetyp_GeraetetypID, ValueListProperty: 'GeraetetypID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Bezeichnung'},
    ],
    SearchSupported : true
  };

  GeraeteID @Common.ValueList: {
    CollectionPath : 'GVerbindung',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: to_Geraetetyp_GeraetetypID,    ValueListProperty: 'GeraetetypID'},
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: GeraeteID, ValueListProperty: 'GeraeteID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'to_Geraetetyp/Bezeichnung'},
    ],
    SearchSupported : true,
  }; 
}


annotate my.GVerbindung {

  GeraetetypID @Common.ValueList: {
    CollectionPath : 'Geraetetyp',
    Label : '',
    Parameters : [
      {$Type: 'Common.ValueListParameterInOut', LocalDataProperty: GeraetetypID, ValueListProperty: 'GeraetetypID'},
      {$Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Bezeichnung'},
    ],
    SearchSupported : true
  };

}


annotate my.Patient {

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



