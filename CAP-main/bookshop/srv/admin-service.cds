using {sap.krc.bookshop as my} from '../db/schema';

service AdminService @(requires_ : 'authenticated-user') {
    entity Books   as projection on my.Books;
    entity Authors as projection on my.Authors;
    entity Orders  as projection on my.Orders;
    //Enable Fiori Draft  for Orders
    annotate AdminService.Orders with @odata.draft.enabled;


}
