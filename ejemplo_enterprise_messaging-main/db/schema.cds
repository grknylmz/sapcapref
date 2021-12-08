using { Currency, managed, cuid } from '@sap/cds/common';
namespace sap.capire.bookshop;

entity Orders : cuid, managed {
  OrderNo  : String @title:'Order Number'; //> readable key
  Items    : Composition of many OrderItems on Items.parent = $self;
  currency : Currency;
}

entity OrderItems : cuid {
  parent    : Association to Orders;
  amount    : Integer;
  netAmount : Decimal(9,2);
}


entity ProjectElemDetail : cuid, managed {
  ObjectName: String;
  HierarchyNodeLevel: Integer ;
  DrillDownState: String ;
  Magnitude: Integer;
  StartDate: Date;
  EndDate: Date;
  parent    : Association to ProjectElemDetail;
  Relationships: Composition of many ProjectElemDetail on Relationships.parent = $self;
  WorkingTimes: Composition of many WorkingTimeDetail on WorkingTimes.parent = $self;
}


entity WorkingTimeDetail : cuid {
  parent    : Association to ProjectElemDetail;
  WorkingTimeID: String ;
  CalendarName: String ;
}


entity CalendarDetail : cuid {

  CalendarName: String ;
  CalendarInterval: Composition of many CalendarIntervalDetail on CalendarInterval.parent = $self;
}

entity CalendarIntervalDetail : cuid {

  StartDate: Date;
  EndDate: Date;
  parent    : Association to CalendarDetail;
}


