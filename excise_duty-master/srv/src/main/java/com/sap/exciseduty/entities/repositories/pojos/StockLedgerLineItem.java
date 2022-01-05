package com.sap.exciseduty.entities.repositories.pojos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sap.exciseduty.utility.GetAsJSONObject;

public class StockLedgerLineItem implements GetAsJSONObject {

    private String materialDocumentYear;
    private String materialDocumentNumber;
    private String materialDocumentItem;
    private int exciseDutyPositionNumber;
    private String exciseDutyTypeId;
    private String companyCode;
    private String taxWarehouseRegistration;
    private String plant;
    private String storageLocation;
    private LocalDate materialDocumentPostingDate;
    private String stockLedgerNumber;
    private String noEntryInExtendedStockLedger;
    private String materialNumber;
    private String batchNumber;
    private BigDecimal quantity;
    private String baseUnitOfMeasure;
    private String edMovementCategoryId;
    private String erpMovementType;
    private String stockLedgerGroupId;
    private String stockLedgerDivision;
    private String stockLedgerSubdivision;
    private BigDecimal alcoholicStrength;
    private String taxWarehouseRegistrationOfManufacturingPlant;
    private String exciseDutyNumberForTaxWarehouse;
    private String externalTaxWarehouseRegistration;
    private String externalExciseDutyNumber;
    private String salesOrderNumber;
    private String salesOrderItem;
    private String exciseDutyProcurementIndicator;
    private String accountingJournalReference;
    private BigDecimal taxValueAmount;
    private String taxValueCurrency;

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

    public String getExciseDutyTypeId() {
        return exciseDutyTypeId;
    }

    public void setExciseDutyTypeId(String exciseDutyTypeId) {
        this.exciseDutyTypeId = exciseDutyTypeId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getTaxWarehouseRegistration() {
        return taxWarehouseRegistration;
    }

    public void setTaxWarehouseRegistration(String taxWarehouseRegistration) {
        this.taxWarehouseRegistration = taxWarehouseRegistration;
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

    public String getStockLedgerNumber() {
        return stockLedgerNumber;
    }

    public void setStockLedgerNumber(String stockLedgerNumber) {
        this.stockLedgerNumber = stockLedgerNumber;
    }

    public String getNoEntryInExtendedStockLedger() {
        return noEntryInExtendedStockLedger;
    }

    public void setNoEntryInExtendedStockLedger(String noEntryInExtendedStockLedger) {
        this.noEntryInExtendedStockLedger = noEntryInExtendedStockLedger;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getBaseUnitOfMeasure() {
        return baseUnitOfMeasure;
    }

    public void setBaseUnitOfMeasure(String baseUnitOfMeasure) {
        this.baseUnitOfMeasure = baseUnitOfMeasure;
    }

    public String getEdMovementCategoryId() {
        return edMovementCategoryId;
    }

    public void setEdMovementCategoryId(String edMovementCategoryId) {
        this.edMovementCategoryId = edMovementCategoryId;
    }

    public String getErpMovementType() {
        return erpMovementType;
    }

    public void setErpMovementType(String erpMovementType) {
        this.erpMovementType = erpMovementType;
    }

    public String getStockLedgerGroupId() {
        return stockLedgerGroupId;
    }

    public void setStockLedgerGroupId(String stockLedgerGroupId) {
        this.stockLedgerGroupId = stockLedgerGroupId;
    }

    public String getStockLedgerDivision() {
        return stockLedgerDivision;
    }

    public void setStockLedgerDivision(String stockLedgerDivision) {
        this.stockLedgerDivision = stockLedgerDivision;
    }

    public String getStockLedgerSubdivision() {
        return stockLedgerSubdivision;
    }

    public void setStockLedgerSubdivision(String stockLedgerSubdivision) {
        this.stockLedgerSubdivision = stockLedgerSubdivision;
    }

    public BigDecimal getAlcoholicStrength() {
        return alcoholicStrength;
    }

    public void setAlcoholicStrength(BigDecimal alcoholicStrength) {
        this.alcoholicStrength = alcoholicStrength;
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

    public String getSalesOrderNumber() {
        return salesOrderNumber;
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        this.salesOrderNumber = salesOrderNumber;
    }

    public String getSalesOrderItem() {
        return salesOrderItem;
    }

    public void setSalesOrderItem(String salesOrderItem) {
        this.salesOrderItem = salesOrderItem;
    }

    public String getExciseDutyProcurementIndicator() {
        return exciseDutyProcurementIndicator;
    }

    public void setExciseDutyProcurementIndicator(String exciseDutyProcurementIndicator) {
        this.exciseDutyProcurementIndicator = exciseDutyProcurementIndicator;
    }

    public String getAccountingJournalReference() {
        return accountingJournalReference;
    }

    public void setAccountingJournalReference(String accountingJournalReference) {
        this.accountingJournalReference = accountingJournalReference;
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

    public JsonElement toJson() {
        Gson gson = new Gson();
        return gson.toJsonTree(this);
    }

}
