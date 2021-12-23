namespace sap.capire.bookshop;
using { Currency, managed } from '@sap/cds/common';

entity Books : managed {
  key ID   : Integer;
  title    : localized String(111);
  descr    : localized String(1111);
  stock    : Integer;
  price    : Decimal(9,2);
  currency : Currency;
}