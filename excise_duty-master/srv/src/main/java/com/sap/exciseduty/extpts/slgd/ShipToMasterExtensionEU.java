package com.sap.exciseduty.extpts.slgd;

import java.time.LocalDate;

public class ShipToMasterExtensionEU {

    private String customerNumber;
    private String taxWarehouseRegistration;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean outsideEUTaxTerritoryIndicator;
    private Country euCountry1 = new Country();
    private Country euCountry2 = new Country();

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Boolean getOutsideEUTaxTerritoryIndicator() {
        return outsideEUTaxTerritoryIndicator;
    }

    public void setOutsideEUTaxTerritoryIndicator(Boolean outsideEUTaxTerritoryIndicator) {
        this.outsideEUTaxTerritoryIndicator = outsideEUTaxTerritoryIndicator;
    }

    public Country getEuCountry1() {
        return euCountry1;
    }

    public void setEuCountry1(Country euCountry1) {
        this.euCountry1 = euCountry1;
    }

    public Country getEuCountry2() {
        return euCountry2;
    }

    public void setEuCountry2(Country euCountry2) {
        this.euCountry2 = euCountry2;
    }

    public String getTaxWarehouseRegistration() {
        return taxWarehouseRegistration;
    }

    public void setTaxWarehouseRegistration(String taxWarehouseRegistration) {
        this.taxWarehouseRegistration = taxWarehouseRegistration;
    }

}
