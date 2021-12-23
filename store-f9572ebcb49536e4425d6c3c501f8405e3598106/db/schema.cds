namespace epam.sap.dev.kitchen_app.store;

using {managed} from '@sap/cds/common';
using {masterdata} from './master-data';
using {
    sap.common.CodeList,
    Currency
} from './common';

entity Products : managed {
    key productUUID          : UUID;
        productID            : String(15);
        phase                : Association to one masterdata.Phases;
        price                : Decimal(15, 2);
        taxrate              : Integer;
        height               : Decimal(13, 3);
        depth                : Decimal(13, 3);
        width                : Decimal(13, 3);
        measure              : Association to one masterdata.UnitOfMeasure;
        CurrencyCode         : Currency;
        productNetAmount     : Decimal(15, 2);
        productTaxAmount     : Decimal(15, 2);
        productGrossAmount   : Decimal(15, 2);
        productTotalQuantity : Integer;
        toProductGroup       : Association to one masterdata.ProductGroups;
        @cascade : {all}
        market               : Composition of many Markets
                                   on market.toProduct = $self;
}

entity Markets : managed {
    key marketUUID        : UUID;
        toMarketInfos     : Association to masterdata.MarketInfos;
        toProduct         : Association to Products;
        startDate         : Date;
        endDate           : Date;
        marketStatus      : Boolean;
        marketNetAmount   : Decimal(15, 2);
        marketTaxAmount   : Decimal(15, 2);
        marketGrossAmount : Decimal(15, 2);
        marketTotalQuantity : Integer;
        CurrencyCode      : Currency;
        @cascade : {all}
        order             : Composition of many Orders
                                on order.toMarket = $self;
}

entity Orders : managed {
    key orderUUID    : UUID;
        toMarket     : Association to Markets;
        orderID : Integer;
        quantity     : Integer;
        calendarYear : String;
        deliveryDate : Date;
        orderNetAmount   : Decimal(15, 2);
        orderTaxAmount   : Decimal(15, 2);
        oderGrossAmount : Decimal(15, 2);
        CurrencyCode      : Currency;
}
