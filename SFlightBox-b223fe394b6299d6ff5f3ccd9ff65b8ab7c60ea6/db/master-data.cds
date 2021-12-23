using { Currency, Country, custom.managed, sap } from './common';
namespace sap.fe.cap.travel;

aspect MasterData {}


entity Airline : MasterData {
  key AirlineID : String(3);
  Name          : String(40);
  AirlinePicURL : String      @UI : {IsImageURL : true};
};

entity Flight : MasterData {
  // TODO:
  // when cuid is added, the to_Airline & to_Connection can be made managed association,
  // furthermore the AirlineID and ConnectionID can be removed,
  // they will be replaced by the generate FKs for to_Airline & to_Connection
  key AirlineID    : String(3);
  key FlightDate   : Date;
  key ConnectionID : String(4);
  to_Airline       : Association to Airline on to_Airline.AirlineID = AirlineID;

};

// showcasing unique constrains ??
// @assert.unique.email: [EMailAddress]
entity Passenger : managed, MasterData {
  key CustomerID : String(6);
  FirstName      : String(40);
  LastName       : String(40);
  Title          : String(10);
  Street         : String(60);
  PostalCode     : String(10);
  City           : String(40);
  CountryCode    : Country;
  PhoneNumber    : String(30);
  EMailAddress   : String(256);
};


