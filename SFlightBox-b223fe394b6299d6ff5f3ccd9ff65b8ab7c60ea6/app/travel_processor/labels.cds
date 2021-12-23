using { sap.fe.cap.travel as schema } from '../../db/schema';

//
// annotations that control rendering of fields and labels
//

annotate schema.Box with @title: '{i18n>Box}' {
  BoxUUID   @UI.Hidden;
  BoxID     @title: '{i18n>TravelID}';
  BeginDateAusleihe    @title: '{i18n>BeginDate}';
  EndDateAusleihe      @title: '{i18n>EndDate}';
  Boxname  @title: '{i18n>Description}';
  BoxStatus @title: '{i18n>TravelStatus}'  @Common.Text: BoxStatus.name     @Common.TextArrangement: #TextOnly;
  to_Customer  @title: '{i18n>CustomerID}'    @Common.Text: to_Customer.LastName  @Common.TextArrangement: #TextFirst;
}

annotate schema.BoxStatus with {
  code @Common.Text: name @Common.TextArrangement: #TextOnly
}

annotate schema.Geraete with @title: '{i18n>Geraete}' {
  GeraeteUUID   @UI.Hidden;
  to_Box     @UI.Hidden;
  GeraeteID     @title: '{i18n>BookingID}';
  ConnectionID  @title: '{i18n>ConnectionID}';
  GeraeteStatus @title: '{i18n>BookinStatus}'  @Common.Text: GeraeteStatus.name    @Common.TextArrangement: #TextOnly;
  to_Carrier    @title: '{i18n>AirlineID}'      @Common.Text: to_Carrier.Name       @Common.TextArrangement: #TextFirst;
  to_Customer   @title: '{i18n>CustomerID}'     @Common.Text: to_Customer.LastName  @Common.TextArrangement: #TextFirst;
}

annotate schema.GeraeteStatus with {
  code @Common.Text : name @Common.TextArrangement: #TextOnly
}

annotate schema.Passenger with @title: '{i18n>Passenger}' {
  CustomerID   @title: '{i18n>CustomerID}'    @Common.Text: LastName @Common.TextArrangement: #TextFirst;
  FirstName    @title: '{i18n>FirstName}';
  LastName     @title: '{i18n>LastName}';
  Title        @title: '{i18n>Title}';
  Street       @title: '{i18n>Street}';
  PostalCode   @title: '{i18n>PostalCode}';
  City         @title: '{i18n>City}';
  CountryCode  @title: '{i18n>CountryCode}';
  PhoneNumber  @title: '{i18n>PhoneNumber}';
  EMailAddress @title: '{i18n>EMailAddress}';
}

annotate schema.Airline with @title: '{i18n>Airline}' {
  AirlineID    @title: '{i18n>AirlineID}'     @Common.Text: Name @Common.TextArrangement: #TextFirst;
  Name         @title: '{i18n>Name}';
}

annotate schema.Flight with @title: '{i18n>Flight}' {
  AirlineID     @title: '{i18n>AirlineID}';
  FlightDate    @title: '{i18n>FlightDate}';
  ConnectionID  @title: '{i18n>ConnectionID}';
}

