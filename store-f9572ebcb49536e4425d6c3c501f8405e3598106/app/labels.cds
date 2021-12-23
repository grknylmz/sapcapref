using epam.sap.dev.kitchen_app.store as schema from '../db/schema';

annotate schema.Products with @title: '{i18n>productTitle}' {
  productUUID   @UI.Hidden;
  productID     @title: '{i18n>ProductID}';
  height    @title: '{i18n>height}' @Measures.Unit : measure_msehi;
  depth      @title: '{i18n>depth}' @Measures.Unit : measure_msehi;
  width  @title: '{i18n>width}' @Measures.Unit : measure_msehi;
  price   @title: '{i18n>price}'    @Measures.ISOCurrency: CurrencyCode_code;
  productNetAmount   @title: '{i18n>NetAmount}'    @Measures.ISOCurrency: CurrencyCode_code;
  productTaxAmount  @title: '{i18n>TaxAmount}'    @Measures.ISOCurrency: CurrencyCode_code;
  productGrossAmount @title: '{i18n>GrossAmount}'    @Measures.ISOCurrency: CurrencyCode_code;
  taxrate @title: '{i18n>taxrate}';
  CurrencyCode  @title: '{i18n>CurrencyCode}';
  phase  @title: '{i18n>phase}' @Common.Text: phase.name    @Common.TextArrangement: #TextFirst;
  toProductGroup @title : '{i18n>ProductName}' @Common.Text: toProductGroup.name    @Common.TextArrangement: #TextLast; 
  measure @title : 'i18n>measure';
  productTotalQuantity @title : '{i18n>totalQuantity}';
}

 annotate ProductService.Markets with {
    startDate @title: '{i18n>startDate}';
    endDate @title: '{i18n>endDate}';
    marketStatus @title: '{i18n>Marketstatus}';
    marketNetAmount @title: '{i18n>NetAmount}' @Measures.ISOCurrency: CurrencyCode_code;
    marketGrossAmount @title: '{i18n>GrossAmount}' @Measures.ISOCurrency: CurrencyCode_code;
    marketTaxAmount @title: '{i18n>TaxAmount}' @Measures.ISOCurrency: CurrencyCode_code;
    currency_code @title: '{i18n>currency}';
    marketTotalQuantity @title: '{i18n>totalQuantity}';
    toMarketInfos @title: '{i18n>market}' @Common.Text: toMarketInfos.name    @Common.TextArrangement: #TextFirst;
};

annotate ProductService.Orders with {
    orderID @title: '{i18n>orderID}';
    deliveryDate @title: '{i18n>deliveryDate}';
    calendarYear @title: '{i18n>year}';
    quantity @title : '{i18n>quantity}';
    orderNetAmount @title: '{i18n>NetAmount}' @Measures.ISOCurrency: CurrencyCode_code;
    oderGrossAmount @title: '{i18n>GrossAmount}' @Measures.ISOCurrency: CurrencyCode_code;
    orderTaxAmount @title: '{i18n>TaxAmount}' @Measures.ISOCurrency: CurrencyCode_code;
    currency_code @title: '{i18n>currency}';
};
