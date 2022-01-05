package com.sap.exciseduty.odata.actions.pojo;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

public class TaxReportingResult {
    private String exciseDutyTypeId;
    private BigDecimal alcoholicStrength;
    private String exciseDutyProcurementIndicator;
    private BigDecimal taxRelevantAmount;
    private BigDecimal notTaxRelevantAmount;
    private BigDecimal thirdCoundryNotTaxRelevantAmount;
    private String taxValueCurrency;

    public String getExciseDutyTypeId() {
        return exciseDutyTypeId;
    }

    public void setExciseDutyTypeId(String exciseDutyTypeId) {
        this.exciseDutyTypeId = exciseDutyTypeId;
    }

    public BigDecimal getAlcoholicStrength() {
        return alcoholicStrength;
    }

    public void setAlcoholicStrength(BigDecimal alcoholicStrength) {
        this.alcoholicStrength = alcoholicStrength;
    }

    public String getExciseDutyProcurementIndicator() {
        return exciseDutyProcurementIndicator;
    }

    public void setExciseDutyProcurementIndicator(String exciseDutyProcurementIndicator) {
        this.exciseDutyProcurementIndicator = exciseDutyProcurementIndicator;
    }

    public BigDecimal getTaxRelevantAmount() {
        return taxRelevantAmount;
    }

    public void setTaxRelevantAmount(BigDecimal taxRelevantAmount) {
        this.taxRelevantAmount = taxRelevantAmount;
    }

    public BigDecimal getNotTaxRelevantAmount() {
        return notTaxRelevantAmount;
    }

    public void setNotTaxRelevantAmount(BigDecimal notTaxRelevantAmount) {
        this.notTaxRelevantAmount = notTaxRelevantAmount;
    }

    public BigDecimal getThirdCoundryNotTaxRelevantAmount() {
        return thirdCoundryNotTaxRelevantAmount;
    }

    public void setThirdCoundryNotTaxRelevantAmount(BigDecimal thirdCoundryNotTaxRelevantAmount) {
        this.thirdCoundryNotTaxRelevantAmount = thirdCoundryNotTaxRelevantAmount;
    }

    public String getTaxValueCurrency() {
        return taxValueCurrency;
    }

    public void setTaxValueCurrency(String taxValueCurrency) {
        this.taxValueCurrency = taxValueCurrency;
    }

    public Map<String, Object> toTypeData() {
        Map<String, Object> result = new HashedMap<String, Object>();
        result.put("exciseDutyTypeId", exciseDutyTypeId);
        result.put("alcoholicStrength", alcoholicStrength);
        result.put("exciseDutyProcurementIndicator", exciseDutyProcurementIndicator);
        result.put("taxRelevantAmount", taxRelevantAmount);
        result.put("notTaxRelevantAmount", notTaxRelevantAmount);
        result.put("thirdCoundryNotTaxRelevantAmount", thirdCoundryNotTaxRelevantAmount);
        result.put("taxValueCurrency", taxValueCurrency);
        return result;
    }
}
