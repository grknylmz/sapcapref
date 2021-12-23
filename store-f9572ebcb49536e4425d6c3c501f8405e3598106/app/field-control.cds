using ProductService from '../srv/services';

annotate cds.UUID with @Core.Computed  @odata.Type : 'Edm.String';
annotate ProductService.Products with {
    price @mandatory;
    productID @mandatory;
    depth @mandatory;
    width @mandatory;
    height @mandatory;
    taxrate @mandatory;
    measure @mandatory;
    toProductGroup @mandatory;
    CurrencyCode @mandatory;
    phase @readonly;
    productGrossAmount @readonly;
    productNetAmount @readonly;
    productTotalQuantity @readonly;
    productTaxAmount @readonly;
}

annotate ProductService.Markets with{
    toMarketInfos @mandatory;
    startDate @mandatory;
    marketGrossAmount @readonly;
    marketNetAmount @readonly;
    marketTaxAmount @readonly;
    marketTotalQuantity @readonly;
} ;

annotate ProductService.Orders with {
    orderID @readonly;
    calendarYear @readonly;
    quantity @mandatory;
    orderNetAmount @readonly;
    orderTaxAmount @readonly;
    oderGrossAmount @readonly;
};




