namespace my.company;

using {managed} from '@sap/cds/common';
using my.company.Customer from './customer';
using my.company.Product from './product';

context Transaction {

    entity Header : managed {
        key ID   : Integer;
        date     : Date not null;
        customer : Association[1] to Customer { ID };
        items    : Association[*] to Item on items.header.ID = ID;
    }

    entity Item {
        key ID   : Integer;
        header   : Association[1] to Header { ID };
        product  : Association[1] to Product { ID };
        @Comment : 'Net Price'
        price    : Decimal(10,2);
        currency : String(3) not null default 'BRL';
    }

}