package com.sap.exciseduty.entities.repositories.pojos;

import java.time.LocalDate;

public class StockLedgerProcessingError {

    private String materialDocumentYear;
    private String materialDocumentNumber;
    private String materialDocumentItem;
    private int exciseDutyPositionNumber;
    private String companyCode;
    private String plant;
    private String storageLocation;
    private LocalDate materialDocumentPostingDate;
    private String materialNumber;
    private String errorText;

    public String getMaterialDocumentYear() {
        return materialDocumentYear;
    }

    public void setMaterialDocumentYear(String materialDocumentYear) {
        this.materialDocumentYear = materialDocumentYear;
    }

    public String getMaterialDocumentNumber() {
        return materialDocumentNumber;
    }

    public void setMaterialDocumentNumber(String materialDocumentNumber) {
        this.materialDocumentNumber = materialDocumentNumber;
    }

    public String getMaterialDocumentItem() {
        return materialDocumentItem;
    }

    public void setMaterialDocumentItem(String materialDocumentItem) {
        this.materialDocumentItem = materialDocumentItem;
    }

    public int getExciseDutyPositionNumber() {
        return exciseDutyPositionNumber;
    }

    public void setExciseDutyPositionNumber(int exciseDutyPositionNumber) {
        this.exciseDutyPositionNumber = exciseDutyPositionNumber;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

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

    public LocalDate getMaterialDocumentPostingDate() {
        return materialDocumentPostingDate;
    }

    public void setMaterialDocumentPostingDate(LocalDate materialDocumentPostingDate) {
        this.materialDocumentPostingDate = materialDocumentPostingDate;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        if (errorText.length() > 40) {
            this.errorText = errorText.substring(0, 40);
        } else {
            this.errorText = errorText;
        }

    }

}
