package com.sap.exciseduty.entities.repositories.pojos;

public class MovementTypeMapping {

    private String erpMovementType;
    private String erpMovementIndicator;
    private String edMovementCategoryId;

    public String getErpMovementType() {
        return erpMovementType;
    }

    public void setErpMovementType(String erpMovementType) {
        this.erpMovementType = erpMovementType;
    }

    public String getErpMovementIndicator() {
        return erpMovementIndicator;
    }

    public void setErpMovementIndicator(String erpMovementIndicator) {
        this.erpMovementIndicator = erpMovementIndicator;
    }

    public String getEdMovementCategoryId() {
        return edMovementCategoryId;
    }

    public void setEdMovementCategoryId(String edMovementCategoryId) {
        this.edMovementCategoryId = edMovementCategoryId;
    }

}
