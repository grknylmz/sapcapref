using { slah.db as schema } from '../../db/schema';

//
// annotations that control rendering of fields and labels
//

annotate schema.Box with @title: '{i18n>Box}' {
  BoxUUID   @UI.Hidden;
  BoxID     @title: '{i18n>BoxID}';
  BeginDateAusleihe    @title: '{i18n>BeginDateAusleihe}';
  EndDateAusleihe      @title: '{i18n>EndDateAusleihe}';
  Boxname  @title: '{i18n>Boxname}';
  BoxStatus @title: '{i18n>BoxStatus}'  @Common.Text: BoxStatus.name     @Common.TextArrangement: #TextOnly;
  to_Patient  @title: '{i18n>PatientID}'    @Common.Text: to_Patient.Nachname  @Common.TextArrangement: #TextFirst;
}

annotate schema.BoxStatus with {
  code @Common.Text: name @Common.TextArrangement: #TextOnly
}

annotate schema.Geraete with @title: '{i18n>Geraete}' {
  GeraeteUUID   @UI.Hidden;
  to_Box     @UI.Hidden;
  GeraeteID     @title: '{i18n>GeraeteID}';
  GeraeteStatus @title: '{i18n>GeraeteStatus}'  @Common.Text: GeraeteStatus.name    @Common.TextArrangement: #TextOnly;
  to_Geraetetyp    @title: '{i18n>GeraetetypID}'      @Common.Text: to_Geraetetyp.Bezeichnung       @Common.TextArrangement: #TextFirst;
  to_Patient   @title: '{i18n>PatientID}'     @Common.Text: to_Patient.Nachname  @Common.TextArrangement: #TextFirst;
}

annotate schema.GeraeteStatus with {
  code @Common.Text : name @Common.TextArrangement: #TextOnly
}

annotate schema.Patient with @title: '{i18n>Patient}' {
  PatientID   @title: '{i18n>PatientID}'    @Common.Text: Nachname @Common.TextArrangement: #TextFirst;
  Vorname    @title: '{i18n>Vorname}';
  Nachname     @title: '{i18n>Nachname}';
  Anrede        @title: '{i18n>Anrede}';
  Strasse       @title: '{i18n>Strasse}';
  Plz   @title: '{i18n>Plz}';
  Stadt         @title: '{i18n>Stadt}';
  CountryCode  @title: '{i18n>CountryCode}';
  Telefonnr  @title: '{i18n>Telefonnr}';
  EMail @title: '{i18n>EMail}';
}

annotate schema.Geraetetyp with @title: '{i18n>Geraetetyp}' {
  GeraetetypID    @title: '{i18n>GeraetetypID}'     @Common.Text: Bezeichnung @Common.TextArrangement: #TextFirst;
  Bezeichnung         @title: '{i18n>Bezeichnung}';
}

annotate schema.GVerbindung with @title: '{i18n>GVerbindung}' {
  GeraetetypID     @title: '{i18n>GeraetetypID}';
  GeraeteID  @title: '{i18n>GeraeteID}';
}


