package com.sap.exciseduty.entities.repositories.pojos;

import java.time.LocalDate;

import com.sap.exciseduty.utility.GetAsJSONObject;

public class TaxWarehouse implements GetAsJSONObject {

    private String exciseDutyTypeId;
    private String taxWarehouseRegistration;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String description;
    private String companyCode;
    private boolean useStockLedgerSubdivisions;
    private String stockLedgerType;

    public String getExciseDutyTypeId() {
        return exciseDutyTypeId;
    }

    public void setExciseDutyTypeId(String exciseDutyTypeId) {
        this.exciseDutyTypeId = exciseDutyTypeId;
    }

    public String getTaxWarehouseRegistration() {
        return taxWarehouseRegistration;
    }

    public void setTaxWarehouseRegistration(String taxWarehouseRegistration) {
        this.taxWarehouseRegistration = taxWarehouseRegistration;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public boolean getUseStockLedgerSubdivisions() {
        return useStockLedgerSubdivisions;
    }

    public void setUseStockLedgerSubdivisions(boolean useStockLedgerSubdivisions) {
        this.useStockLedgerSubdivisions = useStockLedgerSubdivisions;
    }

    public String getStockLedgerType() {
        return stockLedgerType;
    }

    public void setStockLedgerType(String stockLedgerType) {
        this.stockLedgerType = stockLedgerType;
    }

}
