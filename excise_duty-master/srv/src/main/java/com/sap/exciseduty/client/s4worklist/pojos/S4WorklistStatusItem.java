package com.sap.exciseduty.client.s4worklist.pojos;

public class S4WorklistStatusItem {

    private String worklistItemProcessingStatus;
    private String materialDocumentYear;
    private String materialDocumentNumber;
    private String materialDocumentItem;
    private String billOfMaterialItemNodeNumber;

    public S4WorklistStatusItem(S4WorklistItem worklistItem, String status) {
        this.materialDocumentYear = worklistItem.getMaterialYear();
        this.materialDocumentNumber = worklistItem.getMaterialDocumentNumber();
        this.materialDocumentItem = worklistItem.getLine();
        this.billOfMaterialItemNodeNumber = worklistItem.getBillOfMaterialItemNodeNumber();
        this.worklistItemProcessingStatus = status;
    }

    public String getMaterialDocumentYear() {
        return materialDocumentYear;
    }

    public String getWorklistItemProcessingStatus() {
        return worklistItemProcessingStatus;
    }

    public String getMaterialDocumentNumber() {
        return materialDocumentNumber;
    }

    public String getMaterialDocumentItem() {
        return materialDocumentItem;
    }

    public String getBillOfMaterialItemNodeNumber() {
        return billOfMaterialItemNodeNumber;
    }

}
