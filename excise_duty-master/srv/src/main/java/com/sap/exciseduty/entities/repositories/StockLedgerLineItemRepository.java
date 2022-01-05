package com.sap.exciseduty.entities.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.EntityDataBuilder;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IStockLedgerLineItem;
import com.sap.exciseduty.entities.repositories.exceptions.EntityDeletionFailedException;
import com.sap.exciseduty.entities.repositories.exceptions.EntityDuplicationException;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerLineItem;

public class StockLedgerLineItemRepository {

    private DataSourceHandler handler;

    public StockLedgerLineItemRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public void insert(StockLedgerLineItem lineItem) throws EntityDuplicationException {

        EntityData lineItemEntityData = mapPojo(lineItem);

        try {
            handler.executeInsert(lineItemEntityData, false);
        } catch (Exception e) {
            if (e.getMessage().contains("[301]: unique constraint violated")) {
                throw new EntityDuplicationException(IStockLedgerLineItem.NAME);
            }
            // ToDo: Throw better exception
            throw new InternalServerErrorException(e);
        }
    }

    public void update(StockLedgerLineItem lineItem) {

        EntityData lineItemEntityData = mapPojo(lineItem);

        Map<String, Object> keys = new HashMap<>();
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER, lineItem.getMaterialDocumentNumber());
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR, lineItem.getMaterialDocumentYear());
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM, lineItem.getMaterialDocumentItem());
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID, lineItem.getExciseDutyTypeId());
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER, lineItem.getExciseDutyPositionNumber());

        try {
            handler.executeUpdate(lineItemEntityData, keys, false);
            return;
        } catch (Exception e1) {
            throw new InternalServerErrorException(e1);
        }
    }

    // added for WORKAROUND for GATEWAY 1.21.1 + S4SDK 2.4.2 (can be removed)
    public boolean exists(StockLedgerLineItem lineItem) {

        boolean exists = false;

        Map<String, Object> keys = new HashMap<>();
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER, lineItem.getMaterialDocumentNumber());
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR, lineItem.getMaterialDocumentYear());
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM, lineItem.getMaterialDocumentItem());
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID, lineItem.getExciseDutyTypeId());
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER, lineItem.getExciseDutyPositionNumber());

        ArrayList<String> flattenedElementNames = new ArrayList<>();
        flattenedElementNames.add(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER);
        flattenedElementNames.add(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR);
        flattenedElementNames.add(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM);
        flattenedElementNames.add(IStockLedgerLineItem.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID);
        flattenedElementNames.add(IStockLedgerLineItem.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER);

        try {
            EntityData resultEntity = handler.executeRead(IExciseDutyEntities.MODEL_NAME + "." + IStockLedgerLineItem.NAME, keys, flattenedElementNames);
            if (resultEntity != null) {
                Map<String, Object> resultMap = resultEntity.asMap();
                if (resultMap != null) {
                    exists = true;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
        return exists;
    }

    public void deleteAllForMaterialDocumentLine(String materialDocumentNumber, String materialDocumentYear, String materialDocumentItem) throws EntityDeletionFailedException {
        Map<String, Object> keys = new HashMap<>();
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER, materialDocumentNumber);
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR, materialDocumentYear);
        keys.put(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM, materialDocumentItem);
        try {
            handler.executeDelete(IExciseDutyEntities.MODEL_NAME + "." + IStockLedgerLineItem.NAME, keys);
        } catch (Exception e) {
            throw new EntityDeletionFailedException();
        }
    }

    protected EntityData mapPojo(StockLedgerLineItem lineItem) {
        EntityDataBuilder builder = EntityData.getBuilder();
        builder.addKeyElement(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER, lineItem.getMaterialDocumentNumber())
                .addKeyElement(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR, lineItem.getMaterialDocumentYear())
                .addKeyElement(IStockLedgerLineItem.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM, lineItem.getMaterialDocumentItem())
                .addKeyElement(IStockLedgerLineItem.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID, lineItem.getExciseDutyTypeId())
                .addKeyElement(IStockLedgerLineItem.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER, lineItem.getExciseDutyPositionNumber())
                .addElement(IStockLedgerLineItem.ELEMENT_COMPANY_CODE, lineItem.getCompanyCode())
                .addElement(IStockLedgerLineItem.ELEMENT_TAX_WAREHOUSE_REGISTRATION, lineItem.getTaxWarehouseRegistration())
                .addElement(IStockLedgerLineItem.ELEMENT_PLANT, lineItem.getPlant())
                .addElement(IStockLedgerLineItem.ELEMENT_STORAGE_LOCATION, lineItem.getStorageLocation())
                .addElement(IStockLedgerLineItem.ELEMENT_MATERIAL_DOCUMENT_POSTING_DATE, java.sql.Date.valueOf(lineItem.getMaterialDocumentPostingDate()))
                .addElement(IStockLedgerLineItem.ELEMENT_STOCK_LEDGER_NUMBER, lineItem.getStockLedgerNumber())
                .addElement(IStockLedgerLineItem.ELEMENT_NO_ENTRY_IN_EXTENDED_STOCK_LEDGER, lineItem.getNoEntryInExtendedStockLedger())
                .addElement(IStockLedgerLineItem.ELEMENT_MATERIAL_NUMBER, lineItem.getMaterialNumber())
                .addElement(IStockLedgerLineItem.ELEMENT_BATCH_NUMBER, lineItem.getBatchNumber())
                .addElement(IStockLedgerLineItem.ELEMENT_QUANTITY, lineItem.getQuantity())
                .addElement(IStockLedgerLineItem.ELEMENT_BASE_UNIT_OF_MEASURE, lineItem.getBaseUnitOfMeasure())
                .addElement(IStockLedgerLineItem.ELEMENT_ED_MOVEMENT_CATEGORY_ID, lineItem.getEdMovementCategoryId())
                .addElement(IStockLedgerLineItem.ELEMENT_ERP_MOVEMENT_TYPE, lineItem.getErpMovementType())
                .addElement(IStockLedgerLineItem.ELEMENT_STOCK_LEDGER_GROUPE_ID, lineItem.getStockLedgerGroupId())
                .addElement(IStockLedgerLineItem.ELEMENT_STOCK_LEDGER_DEVISION, lineItem.getStockLedgerDivision())
                .addElement(IStockLedgerLineItem.ELEMENT_STOCK_LEDGER_SUBDIVISION, lineItem.getStockLedgerSubdivision())
                .addElement(IStockLedgerLineItem.ELEMENT_ALCOHOLIG_STRENGHT, lineItem.getAlcoholicStrength())
                .addElement(IStockLedgerLineItem.ELEMENT_TAX_WAREHOUSE_REGISTRATION_OF_MANUFACTURING_PLANT, lineItem.getTaxWarehouseRegistrationOfManufacturingPlant())
                .addElement(IStockLedgerLineItem.ELEMENT_EXCISE_DUTY_NUMBER_FOR_TAX_WAREHOUSE, lineItem.getExciseDutyNumberForTaxWarehouse())
                .addElement(IStockLedgerLineItem.ELEMENT_EXTERNAL_TAX_WAREHOUSE_REGISTRATION, lineItem.getExternalTaxWarehouseRegistration())
                .addElement(IStockLedgerLineItem.ELEMENT_EXTERNAL_EXCISE_DUTY_NUMBER, lineItem.getExternalExciseDutyNumber())
                .addElement(IStockLedgerLineItem.ELEMENT_SALES_ORDER_NUMBER, lineItem.getSalesOrderNumber())
                .addElement(IStockLedgerLineItem.ELEMENT_SALES_ORDER_ITEM, lineItem.getSalesOrderItem())
                .addElement(IStockLedgerLineItem.ELEMENT_EXCISE_DUTY_PROCUREMENT_INDICATOR, lineItem.getExciseDutyProcurementIndicator())
                .addElement(IStockLedgerLineItem.ELEMENT_ACCOUNTING_JOURNAL_REFERENCE, lineItem.getAccountingJournalReference())
                .addElement(IStockLedgerLineItem.ELEMENT_TAX_VALUE_AMOUNT, lineItem.getTaxValueAmount())
                .addElement(IStockLedgerLineItem.ELEMENT_TAX_VALUE_CURRENCY, lineItem.getTaxValueCurrency());

        EntityData lineItemEntityData = builder.buildEntityData(IExciseDutyEntities.MODEL_NAME + "." + IStockLedgerLineItem.NAME);

        return lineItemEntityData;
    }
}
