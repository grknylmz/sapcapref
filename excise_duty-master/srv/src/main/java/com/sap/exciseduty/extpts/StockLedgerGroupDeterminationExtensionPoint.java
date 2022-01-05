package com.sap.exciseduty.extpts;

import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.entities.repositories.pojos.MaterialMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.ShipToMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerLineItem;
import com.sap.exciseduty.entities.repositories.pojos.TaxWarehouse;
import com.sap.exciseduty.extpts.StockLedgerGroupDeterminationExtensionPoint.Input;
import com.sap.exciseduty.extpts.StockLedgerGroupDeterminationExtensionPoint.Output;
import com.sap.exciseduty.extpts.slgd.CustomerGroupSpecialPartnerAssignment;
import com.sap.exciseduty.extpts.slgd.ShipToMasterExtensionEU;
import com.sap.exciseduty.processors.exceptions.StockLedgerGroupMappingNotFound;
import com.sap.exciseduty.processors.exceptions.StockLedgerGroupNotFoundException;

/**
 * 
 * @author d021248
 *         This extensionPoint will determine the StockLedgerGroup.
 *         (detailed description...)
 * 
 *         Input:
 *         - material master data
 *         - receiver information
 *         (detailed description...)
 * 
 *         Output:
 *         the stock ledger group
 *         (detailed description...)
 *
 *         Exceptions:
 *         - StockLedgerGroupNotFoundException
 *         - StockLedgerGroupMappingNotFound
 *
 */
public interface StockLedgerGroupDeterminationExtensionPoint extends ExtensionPoint<Input, Output> {

    final static String EXTENSION_POINT_NAME = "StockLedgerDet";

    @Override
    public Output call(Input input, Extension<Input, Output> defaultImplementation) throws StockLedgerGroupNotFoundException, StockLedgerGroupMappingNotFound;

    public static class Input {

        private S4WorklistItem s4WorklistItem;
        private StockLedgerLineItem stockLedgerLineItem;
        private ShipToMasterExtension shipToMasterExtension;
        private ShipToMasterExtensionEU shipToMasterExtensionEU;
        private TaxWarehouse taxWarehouse;
        private CustomerGroupSpecialPartnerAssignment customerGroupSpecialPartnerAssignment;
        private MaterialMasterExtension materialMasterExtension;

        public MaterialMasterExtension getMaterialMasterExtension() {
            return materialMasterExtension;
        }

        public void setMaterialMasterExtension(MaterialMasterExtension materialMasterExtension) {
            this.materialMasterExtension = materialMasterExtension;
        }

        public S4WorklistItem getS4WorklistItem() {
            return s4WorklistItem;
        }

        public void setS4WorklistItem(S4WorklistItem s4WorklistItem) {
            this.s4WorklistItem = s4WorklistItem;
        }

        public StockLedgerLineItem getStockLedgerLineItem() {
            return stockLedgerLineItem;
        }

        public void setStockLedgerLineItem(StockLedgerLineItem stockLedgerLineItem) {
            this.stockLedgerLineItem = stockLedgerLineItem;
        }

        public ShipToMasterExtension getShipToMasterExtension() {
            return shipToMasterExtension;
        }

        public void setShipToMasterExtension(ShipToMasterExtension shipToMasterExtension) {
            this.shipToMasterExtension = shipToMasterExtension;
        }

        public ShipToMasterExtensionEU getShipToMasterExtensionEU() {
            return shipToMasterExtensionEU;
        }

        public void setShipToMasterExtensionEU(ShipToMasterExtensionEU shipToMasterExtensionEU) {
            this.shipToMasterExtensionEU = shipToMasterExtensionEU;
        }

        public TaxWarehouse getTaxWarehouse() {
            return taxWarehouse;
        }

        public void setTaxWarehouse(TaxWarehouse taxWarehouse) {
            this.taxWarehouse = taxWarehouse;
        }

        public CustomerGroupSpecialPartnerAssignment getCustomerGroupSpecialPartnerAssignment() {
            return customerGroupSpecialPartnerAssignment;
        }

        public void setCustomerGroupSpecialPartnerAssignment(CustomerGroupSpecialPartnerAssignment customerGroupSpecialPartnerAssignment) {
            this.customerGroupSpecialPartnerAssignment = customerGroupSpecialPartnerAssignment;
        }
    }

    public static class Output {

        private String stockLedgerGroupId;
        private String stockLedgerDivision;
        private String stockLedgerSubdivision;
        private String stockLedgerNumber;

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

        public String getStockLedgerNumber() {
            return stockLedgerNumber;
        }

        public void setStockLedgerNumber(String stockLedgerNumber) {
            this.stockLedgerNumber = stockLedgerNumber;
        }
    }
}
