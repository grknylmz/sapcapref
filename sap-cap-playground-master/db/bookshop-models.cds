namespace com.playground.bookshop;

using { Country, managed } from '@sap/cds/common';

entity Books: managed {
  key ID : Integer @title: '{i18n>book}';
  title  : localized String;
  author : Association to Authors @(
    title: '{i18n>author}',
    Common: {
      ValueList: {entity: 'Authors', type: #fixed},
      Taxt: {$value: author.name, "@UI.TextArrangement": #TextOnly},
      FieldControl: #Mandatory
    });
  stock  : String @title: '{i18n>stock}';
  @Core.MediaType: 'image/png'
  photo : LargeBinary;
}

entity Authors {
  key ID : Integer;
  name   : String not null;
  books  : Association to many Books on books.author = $self @title: '{i18n>book}';
}

entity Orders : managed {
  key ID  : UUID;
  book    : Association to Books;
  country : Country;
  amount  : Integer;
}

extend Orders with @title:'Orders' {
  firstCreatedAt : DateTime  @readonly @odata.on.insert: #now;
  firstCreatedBy : String @readonly @odata.on.insert: #user;
  status : Integer enum {
    submitted = 1;
    fulfilled = 2;
    shipped = 3;
    canceled = -1;
  };
}

type AddressType {
     city : String;
     street : String;
     streetNumber : Integer;
}

type Gender : String enum { male; female; }

extend Authors with @title:'Authors' {
  address: AddressType;
  gender: Gender;
}

annotate Authors with {
  	name  @title:'{i18n>name}';
}


type Currency : String(3) @title: 'Currency Code' @ValueList.entity: Currencies;

entity Currencies {
  key code : Currency;
  name : String;
}