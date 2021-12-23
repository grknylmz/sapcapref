using { Currency, custom.managed, sap.common.CodeList } from './common';
using {
  sap.fe.cap.travel.Airline,
  sap.fe.cap.travel.Passenger,
  sap.fe.cap.travel.Flight
 } from './master-data';

namespace sap.fe.cap.travel;

//Travel
entity Box : managed {
  key BoxUUID : UUID;
  BoxID       : Integer @readonly default 0;
  BeginDateAusleihe      : Date;
  EndDateAusleihe        : Date;
  Boxname    : String(1024);
  BoxStatus   : Association to BoxStatus @readonly;
  to_Customer    : Association to Passenger;
  to_Geraete     : Composition of many Geraete on to_Geraete.to_Box = $self;
};

//Booking
entity Geraete  : managed {
  key GeraeteUUID   : UUID;
  GeraeteID         : Integer @Core.Computed;
  ConnectionID      : String(4);
  GeraeteStatus     : Association to GeraeteStatus;
  to_Carrier        : Association to Airline;
  to_Customer       : Association to Passenger;
  to_Box         : Association to Box;
  to_Flight         : Association to Flight on  to_Flight.AirlineID = to_Carrier.AirlineID
                                            and to_Flight.ConnectionID = ConnectionID;
};

//
//  Code Lists
//

entity GeraeteStatus : CodeList {
  key code : String enum {
    New      = 'N';
    Booked   = 'B';
    Canceled = 'X';
  };
};

entity BoxStatus : CodeList {
  key code : String enum {
    Open     = 'O';
    Accepted = 'A';
    Canceled = 'X';
  } default 'O'; //> will be used for foreign keys as well
  criticality : Integer; //  2: yellow colour,  3: green colour, 0: unknown
  fieldControl: Integer @odata.Type:'Edm.Byte'; // 1: #ReadOnly, 7: #Mandatory
  createDeleteHidden: Boolean;
}
