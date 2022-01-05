package com.sap.exciseduty.entities.repositories.pojos;

public class StockLedgerGroup {

    private String id;
    private String description;

    private String stockLedgerDivision;
    private String stockLedgerSubdivision;
    private String movementEntryBehavior;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getMovementEntryBehavior() {
        return movementEntryBehavior;
    }

    public void setMovementEntryBehavior(String movementEntryBehavior) {
        this.movementEntryBehavior = movementEntryBehavior;
    }
}
