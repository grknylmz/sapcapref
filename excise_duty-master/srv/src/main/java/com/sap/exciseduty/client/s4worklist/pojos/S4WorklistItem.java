package com.sap.exciseduty.client.s4worklist.pojos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sap.exciseduty.utility.GetAsJSONObject;

public class S4WorklistItem implements GetAsJSONObject {
    private String mblnr;
    private String mjahr;
    private String zeile;
    private String stlkn;
    private String matnr;
    private String stats;
    private String bwart;
    private String xauto;
    private String werks;
    private String bukrs;
    private String lgort;
    private String charg;
    private String sobkz;
    private String aufnr;
    private String kzbew;
    private String lifnr;
    private String kunnr;
    private String kdauf;
    private String kdpos;
    private String vbeln;
    private String auart;
    private String augru;
    private String pstyv;
    private String kunag;
    private String kuniv;
    private String shkzg;
    private BigDecimal menge;
    private String meins;
    private String ebeln;
    private String ebelp;
    private String vgabe;
    private String belnr;
    private String pstyp;
    private String sgtxt;
    private String wempf;
    private String kostl;
    private LocalDate budat;
    private String zz_kdgrp;
    private String zz_kun_land1;
    private String zz_kun_land1_xegld;

    public String getMaterialDocumentNumber() {
        return mblnr;
    }

    public String getMaterialYear() {
        return mjahr;
    }

    public String getLine() {
        return zeile;
    }

    public String getBillOfMaterialItemNodeNumber() {
        return stlkn;
    }

    public String getMaterialNumber() {
        return matnr;
    }

    public String getProcessingStatus() {
        return stats;
    }

    public String getMovementType() {
        return bwart;
    }

    public String getAutomaticallyCreatedCharacteristic() {
        return xauto;
    }

    public String getPlant() {
        return werks;
    }

    public String getCompanyCode() {
        return bukrs;
    }

    public String getStorageLocation() {
        return lgort;
    }

    public String getBatchNumber() {
        return charg;
    }

    public String getSpecialStock() {
        return sobkz;
    }

    public String getOrderNumber() {
        return aufnr;
    }

    public String getMovementIndicator() {
        return kzbew;
    }

    public String getVendorAccountNumber() {
        return lifnr;
    }

    public String getCustomerNumber() {
        return kunnr;
    }

    public String getSalesOrderNumber() {
        return kdauf;
    }

    public String getSalesOrderItemNumber() {
        return kdpos;
    }

    public String getSalesDocument() {
        return vbeln;
    }

    public String getSalesDocumentType() {
        return auart;
    }

    public String getOrderReason() {
        return augru;
    }

    public String getSalesDocumentItemCategory() {
        return pstyv;
    }

    public String getSoldToParty() {
        return kunag;
    }

    public String getCustomerNumberForIntercompanyBilling() {
        return kuniv;
    }

    public String getDebitorCreditorIndicator() {
        return shkzg;
    }

    public BigDecimal getQuantity() {
        return menge;
    }

    public String getBaseUnitOfMeasure() {
        return meins;
    }

    public String getPurchsaeOrderNumber() {
        return ebeln;
    }

    public String getPurchaseOrderItemNumber() {
        return ebelp;
    }

    public String getPurchaseOrderHistory() {
        return vgabe;
    }

    public String getNumberOfMaterialDocument() {
        return belnr;
    }

    public String getPurchaseDocumentItemCategory() {
        return pstyp;
    }

    public String getItemText() {
        return sgtxt;
    }

    public String getGoodsRecipient() {
        return wempf;
    }

    public String getCostCenter() {
        return kostl;
    }

    public LocalDate getPostingDateInDocument() {
        return budat;
    }

    public void setMaterialDocumentNumber(String materialDocumentNumber) {
        this.mblnr = materialDocumentNumber;
    }

    public void setMaterialYear(String materialYear) {
        this.mjahr = materialYear;
    }

    public void setLine(String line) {
        this.zeile = line;
    }

    public void setBillOfMaterialItemNodeNumber(String billOfMaterialItemNodeNumber) {
        this.stlkn = billOfMaterialItemNodeNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.matnr = materialNumber;
    }

    public void setProcessingStatus(String processingStatus) {
        this.stats = processingStatus;
    }

    public void setMovementType(String movementType) {
        this.bwart = movementType;
    }

    public void setAutomaticallyCreatedCharacteristic(String automaticallyCreatedCharacteristic) {
        this.xauto = automaticallyCreatedCharacteristic;
    }

    public void setPlant(String plant) {
        this.werks = plant;
    }

    public void setCompanyCode(String companyCode) {
        this.bukrs = companyCode;
    }

    public void setStorageLocation(String storageLocation) {
        this.lgort = storageLocation;
    }

    public void setBatchNumber(String batchNumber) {
        this.charg = batchNumber;
    }

    public void setSpecialStock(String specialStock) {
        this.sobkz = specialStock;
    }

    public void setOrderNumber(String orderNumber) {
        this.aufnr = orderNumber;
    }

    public void setMovementIndicator(String movementIndicator) {
        this.kzbew = movementIndicator;
    }

    public void setVendorAccountNumber(String vendorAccountNumber) {
        this.lifnr = vendorAccountNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.kunnr = customerNumber;
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        this.kdauf = salesOrderNumber;
    }

    public void setSalesOrderItemNumber(String salesOrderItemNumber) {
        this.kdpos = salesOrderItemNumber;
    }

    public void setSalesDocument(String salesDocument) {
        this.vbeln = salesDocument;
    }

    public void setSalesDocumentType(String salesDocumentType) {
        this.auart = salesDocumentType;
    }

    public void setOrderReason(String orderReason) {
        this.augru = orderReason;
    }

    public void setSalesDocumentItemCategory(String salesDocumentItemCategory) {
        this.pstyv = salesDocumentItemCategory;
    }

    public void setSoldToParty(String soldToParty) {
        this.kunag = soldToParty;
    }

    public void setCustomerNumberForIntercompanyBilling(String customerNumberForIntercompanyBilling) {
        this.kuniv = customerNumberForIntercompanyBilling;
    }

    public void setDebitorCreditorIndicator(String debitorCreditorIndicator) {
        this.shkzg = debitorCreditorIndicator;
    }

    public void setQuantity(BigDecimal quantity) {
        this.menge = quantity;
    }

    public void setBaseUnitOfMeasure(String baseUnitOfMeasure) {
        this.meins = baseUnitOfMeasure;
    }

    public void setPurchsaeOrderNumber(String purchsaeOrderNumber) {
        this.ebeln = purchsaeOrderNumber;
    }

    public void setPurchaseOrderItemNumber(String purchaseOrderItemNumber) {
        this.ebelp = purchaseOrderItemNumber;
    }

    public void setPurchaseOrderHistory(String purchaseOrderHistory) {
        this.vgabe = purchaseOrderHistory;
    }

    public void setNumberOfMaterialDocument(String numberOfMaterialDocument) {
        this.belnr = numberOfMaterialDocument;
    }

    public void setPurchaseDocumentItemCategory(String purchaseDocumentItemCategory) {
        this.pstyp = purchaseDocumentItemCategory;
    }

    public void setItemText(String itemText) {
        this.sgtxt = itemText;
    }

    public void setGoodsRecipient(String goodsRecipient) {
        this.wempf = goodsRecipient;
    }

    public void setCostCenter(String costCenter) {
        this.kostl = costCenter;
    }

    public void setPostingDateInDocument(LocalDate postingDateInDocument) {
        this.budat = postingDateInDocument;
    }

    public String getZz_kdgrp() {
        return zz_kdgrp;
    }

    public void setZz_kdgrp(String zz_kdgrp) {
        this.zz_kdgrp = zz_kdgrp;
    }

    public String getZz_kun_land1() {
        return zz_kun_land1;
    }

    public void setZz_kun_land1(String zz_kun_land1) {
        this.zz_kun_land1 = zz_kun_land1;
    }

    public String getZz_kun_land1_xegld() {
        return zz_kun_land1_xegld;
    }

    public void setZz_kun_land1_xegld(String zz_kun_land1_xegld) {
        this.zz_kun_land1_xegld = zz_kun_land1_xegld;
    }

}
