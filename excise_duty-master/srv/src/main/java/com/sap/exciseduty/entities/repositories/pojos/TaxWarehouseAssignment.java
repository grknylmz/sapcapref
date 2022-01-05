package com.sap.exciseduty.entities.repositories.pojos;

import java.time.LocalDate;

public class TaxWarehouseAssignment {

    private String plant;
    private String storageLocation;
    private String exciseDutyTypeId;
    private LocalDate validFrom;

    private LocalDate validTo;
    private String taxWarehouseRegistration;

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getExciseDutyTypeId() {
        return exciseDutyTypeId;
    }

    public void setExciseDutyTypeId(String exciseDutyTypeId) {
        this.exciseDutyTypeId = exciseDutyTypeId;
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

    public String getTaxWarehouseRegistration() {
        return taxWarehouseRegistration;
    }

    public void setTaxWarehouseRegistration(String taxWarehouseRegistration) {
        this.taxWarehouseRegistration = taxWarehouseRegistration;
    }

}
