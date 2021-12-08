using { sap.capire.bookshop as my } from '../db/schema';

service OrdersService {
  entity Orders as projection on my.Orders;
  entity ProjectElems as projection on my.ProjectElemDetail;
  entity Calendars as projection on my.CalendarDetail;
}

service EnterpriseMessaging{
  function send() returns String;
}
