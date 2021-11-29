namespace my.company;

using my.company.Transaction from './transaction';

entity Product {
    key ID           : Integer;
    @title: '{i18n>product.name}'
    name             : String not null;
    segment          : String;
    @Comment         : 'Gross Price'
    price            : Decimal(10,2);
    currency         : String(3) not null default 'BRL';
    transactionItems : Association[*] to Transaction.Item on transactionItems.product.ID = ID;
}