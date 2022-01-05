package com.sap.exciseduty.entities.actions;

import java.math.BigDecimal;

import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.exciseduty.entities.IExciseDutyEntities;

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

    public EntityData toEntityData() {
        EntityData result = EntityData.getBuilder()
                .addKeyElement("exciseDutyTypeId", exciseDutyTypeId)
                .addKeyElement("alcoholicStrength", alcoholicStrength)
                .addElement("exciseDutyProcurementIndicator", exciseDutyProcurementIndicator)
                .addElement("taxRelevantAmount", taxRelevantAmount)
                .addElement("notTaxRelevantAmount", notTaxRelevantAmount)
                .addElement("thirdCoundryNotTaxRelevantAmount", thirdCoundryNotTaxRelevantAmount)
                .addElement("taxValueCurrency", taxValueCurrency)
                .buildEntityData(IExciseDutyEntities.STOCK_LEDGER_SERVICE + ".TaxReportingResult");
        return result;
    }
}
