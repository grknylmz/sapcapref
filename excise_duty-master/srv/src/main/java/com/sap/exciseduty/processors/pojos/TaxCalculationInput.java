package com.sap.exciseduty.processors.pojos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sap.exciseduty.entities.repositories.pojos.MaterialMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.SettlementUnit;

public class TaxCalculationInput {

    String materialNumber;
    BigDecimal materialDocumentQuantity;
    String materialDocumentBaseUnitOfMeasure;
    MaterialMasterExtension materialMasterExtension;
    SettlementUnit settlementUnit;
    BusinessTransactionType businessTransactionType;
    LocalDate materialDocumentPostingDate;

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public BigDecimal getMaterialDocumentQuantity() {
        return materialDocumentQuantity;
    }

    public void setMaterialDocumentQuantity(BigDecimal materialDocumentQuantity) {
        this.materialDocumentQuantity = materialDocumentQuantity;
    }

    public String getMaterialDocumentBaseUnitOfMeasure() {
        return materialDocumentBaseUnitOfMeasure;
    }

    public void setMaterialDocumentBaseUnitOfMeasure(String materialDocumentBaseUnitOfMeasure) {
        this.materialDocumentBaseUnitOfMeasure = materialDocumentBaseUnitOfMeasure;
    }

    public MaterialMasterExtension getMaterialMasterExtension() {
        return materialMasterExtension;
    }

    public void setMaterialMasterExtension(MaterialMasterExtension materialMasterExtension) {
        this.materialMasterExtension = materialMasterExtension;
    }

    public SettlementUnit getSettlementUnit() {
        return settlementUnit;
    }

    public void setSettlementUnit(SettlementUnit settlementUnit) {
        this.settlementUnit = settlementUnit;
    }

    public BusinessTransactionType getBusinessTransactionType() {
        return businessTransactionType;
    }

    public void setBusinessTransactionType(BusinessTransactionType businessTransactionType) {
        this.businessTransactionType = businessTransactionType;
    }

    public LocalDate getMaterialDocumentPostingDate() {
        return materialDocumentPostingDate;
    }

    public void setMaterialDocumentPostingDate(LocalDate materialDocumentPostingDate) {
        this.materialDocumentPostingDate = materialDocumentPostingDate;
    }

}
