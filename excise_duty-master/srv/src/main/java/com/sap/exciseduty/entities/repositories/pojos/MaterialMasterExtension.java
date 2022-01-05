package com.sap.exciseduty.entities.repositories.pojos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MaterialMasterExtension {

    private String materialNumber;
    private String companyCode;
    private String exciseDutyTypeId;
    private String exciseDutyProcurementIndicator;
    private String taxWarehouseRegistrationOfManufacturingPlant;
    private String exciseDutyNumberForTaxWarehouse;
    private String exciseDutyTypeIndependentMaterialGroup;
    private BigDecimal alcoholicStrength;
    private LocalDate validFrom;

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getExciseDutyTypeId() {
        return exciseDutyTypeId;
    }

    public void setExciseDutyTypeId(String exciseDutyTypeId) {
        this.exciseDutyTypeId = exciseDutyTypeId;
    }

    public String getExciseDutyProcurementIndicator() {
        return exciseDutyProcurementIndicator;
    }

    public void setExciseDutyProcurementIndicator(String exciseDutyProcurementIndicator) {
        this.exciseDutyProcurementIndicator = exciseDutyProcurementIndicator;
    }

    public String getTaxWarehouseRegistrationOfManufacturingPlant() {
        return taxWarehouseRegistrationOfManufacturingPlant;
    }

    public void setTaxWarehouseRegistrationOfManufacturingPlant(String taxWarehouseRegistrationOfManufacturingPlant) {
        this.taxWarehouseRegistrationOfManufacturingPlant = taxWarehouseRegistrationOfManufacturingPlant;
    }

    public String getExciseDutyNumberForTaxWarehouse() {
        return exciseDutyNumberForTaxWarehouse;
    }

    public void setExciseDutyNumberForTaxWarehouse(String exciseDutyNumberForTaxWarehouse) {
        this.exciseDutyNumberForTaxWarehouse = exciseDutyNumberForTaxWarehouse;
    }

    public String getExciseDutyTypeIndependentMaterialGroup() {
        return exciseDutyTypeIndependentMaterialGroup;
    }

    public void setExciseDutyTypeIndependentMaterialGroup(String exciseDutyTypeIndependentMaterialGroup) {
        this.exciseDutyTypeIndependentMaterialGroup = exciseDutyTypeIndependentMaterialGroup;
    }

    public BigDecimal getAlcoholicStrength() {
        return alcoholicStrength;
    }

    public void setAlcoholicStrength(BigDecimal alcoholicStrength) {
        this.alcoholicStrength = alcoholicStrength;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

}
