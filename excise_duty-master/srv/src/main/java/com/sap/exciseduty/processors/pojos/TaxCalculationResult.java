package com.sap.exciseduty.processors.pojos;

import java.math.BigDecimal;

public class TaxCalculationResult {

    public String exciseDutyTypeId;

    public BigDecimal taxValueAmount;
    public String taxValueCurrency;

    public String getExciseDutyTypeId() {
        return exciseDutyTypeId;
    }

    public void setExciseDutyTypeId(String exciseDutyTypeId) {
        this.exciseDutyTypeId = exciseDutyTypeId;
    }

    public BigDecimal getTaxValueAmount() {
        return taxValueAmount;
    }

    public void setTaxValueAmount(BigDecimal taxValueAmount) {
        this.taxValueAmount = taxValueAmount;
    }

    public String getTaxValueCurrency() {
        return taxValueCurrency;
    }

    public void setTaxValueCurrency(String taxValueCurrency) {
        this.taxValueCurrency = taxValueCurrency;
    }

}
