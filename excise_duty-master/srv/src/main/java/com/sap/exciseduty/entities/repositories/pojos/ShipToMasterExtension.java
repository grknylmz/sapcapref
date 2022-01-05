package com.sap.exciseduty.entities.repositories.pojos;

import java.time.LocalDate;

import com.sap.exciseduty.utility.GetAsJSONObject;

public class ShipToMasterExtension implements GetAsJSONObject {

    private String customerNumber;
    private LocalDate validFrom;

    private LocalDate validTo;
    private String externalTaxWarehouseRegistration;
    private String externalExciseDutyNumber;
    private String exciseDutySpecialPartnerTypeId;
    private boolean thirdCountryIndicator;

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

    public String getExternalTaxWarehouseRegistration() {
        return externalTaxWarehouseRegistration;
    }

    public void setExternalTaxWarehouseRegistration(String externalTaxWarehouseRegistration) {
        this.externalTaxWarehouseRegistration = externalTaxWarehouseRegistration;
    }

    public String getExternalExciseDutyNumber() {
        return externalExciseDutyNumber;
    }

    public void setExternalExciseDutyNumber(String externalExciseDutyNumber) {
        this.externalExciseDutyNumber = externalExciseDutyNumber;
    }

    public String getExciseDutySpecialPartnerTypeId() {
        return exciseDutySpecialPartnerTypeId;
    }

    public void setExciseDutySpecialPartnerTypeId(String exciseDutySpecialPartnerTypeId) {
        this.exciseDutySpecialPartnerTypeId = exciseDutySpecialPartnerTypeId;
    }

    public boolean isThirdCountryIndicator() {
        return thirdCountryIndicator;
    }

    public void setThirdCountryIndicator(boolean thirdCountryIndicator) {
        this.thirdCountryIndicator = thirdCountryIndicator;
    }
}
