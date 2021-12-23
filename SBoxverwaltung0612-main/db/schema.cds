using { Country, managed, sap.common.CodeList } from '@sap/cds/common';


namespace slah.db;

entity Box : managed {
  key BoxUUID       : UUID;
  BoxID             : Integer @Core.Computed;
  BeginDateAusleihe : Date;
  EndDateAusleihe   : Date;
  Boxname           : String(1024);
  BoxStatus         : Association to BoxStatus @readonly;
  to_Patient        : Association to Patient;
  to_Geraete        : Composition of many Geraete on to_Geraete.to_Box = $self;
};


entity Geraete : managed {
  key GeraeteUUID : UUID;
  GeraeteID       : Integer;
  GeraeteStatus   : Association to GeraeteStatus;
  to_Geraetetyp   : Association to Geraetetyp;
  to_Patient      : Association to Patient;
  to_Box          : Association to Box;
  to_GVerbindung  : Association to GVerbindung on to_GVerbindung.GeraetetypID = to_Geraetetyp.GeraetetypID
                                               and to_GVerbindung.GeraeteID = GeraeteID;
};


entity Geraetetyp : managed {
  key GeraetetypID : Integer;
  Bezeichnung      : String(40);
  AnleitungURL     : String @UI : {IsImageURL : true};
};


entity GVerbindung : managed {
  key GeraetetypID : Integer;
  key GeraeteID    : String;
  to_Geraetetyp    : Association to Geraetetyp on to_Geraetetyp.GeraetetypID = GeraetetypID;
  to_Geraete       : Association to Geraete on to_Geraete.GeraeteID = GeraeteID;  
};


entity Patient : managed {
  key PatientID : String(6);
  Vorname       : String(40);
  Nachname      : String(40);
  Anrede        : String(10);
  Strasse       : String(60);
  Plz           : String(10);
  Stadt         : String(40);
  CountryCode   : Country;
  Telefonnr     : String(30);
  EMail         : String(256);
};


//
//  Code Lists
//


entity GeraeteStatus : CodeList {
  key code : String enum {
    Neu             = 'N';     
    Einsatzbereit   = 'E';
    Verfuegbar1     = 'V1';      
    Verfuegbar2     = 'V2';
    Messend         = 'M';
    Gesperrt        = 'G'; 
  };
};


entity BoxStatus : CodeList {
  key code : String enum {
    Verfuegbar     = 'O';
    Rueckgabe      = 'R';
    AusserHaus     = 'X';
  } default 'O'; //> will be used for foreign keys as well
  criticality : Integer; //  2: yellow colour,  3: green colour, 0: unknown
  fieldControl: Integer @odata.Type:'Edm.Byte'; // 1: #ReadOnly, 7: #Mandatory
  createDeleteHidden: Boolean;
}
