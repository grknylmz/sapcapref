package com.sap.exciseduty.entities.repositories;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.EntityDataBuilder;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IStockLedgerProcessingError;
import com.sap.exciseduty.entities.repositories.exceptions.EntityDeletionFailedException;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerProcessingError;

public class StockLedgerProcessingErrorRepository {
    private DataSourceHandler handler;

    public StockLedgerProcessingErrorRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public StockLedgerProcessingError getByKey(String materialDocumentItem, String materialDocumentNumber, String materialDocumentYear, Integer exciseDutyPositionNumber) throws EntityNotFoundException {
        Map<String, Object> keys = new HashMap<>();
        keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER, materialDocumentNumber);
        keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR, materialDocumentYear);
        keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM, materialDocumentItem);
        keys.put(IStockLedgerProcessingError.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER, exciseDutyPositionNumber);

        ArrayList<String> flattenedElementNames = new ArrayList<>();
        flattenedElementNames.add(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER);
        flattenedElementNames.add(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR);
        flattenedElementNames.add(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM);
        flattenedElementNames.add(IStockLedgerProcessingError.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER);
        flattenedElementNames.add(IStockLedgerProcessingError.ELEMENT_COMPANY_CODE);
        flattenedElementNames.add(IStockLedgerProcessingError.ELEMENT_PLANT);
        flattenedElementNames.add(IStockLedgerProcessingError.ELEMENT_STORAGE_LOCATION);
        flattenedElementNames.add(IStockLedgerProcessingError.ELEMENT_MATERIAL_DOCUMENT_POSTING_DATE);
        flattenedElementNames.add(IStockLedgerProcessingError.ELEMENT_MATERIAL_NUMBER);
        flattenedElementNames.add(IStockLedgerProcessingError.ELEMENT_ERROR_TEXT);

        EntityData resultEntity;
        try {
            resultEntity = handler.executeRead(IExciseDutyEntities.MODEL_NAME + "." + IStockLedgerProcessingError.NAME, keys, flattenedElementNames);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }

        if (resultEntity == null) {
            throw new EntityNotFoundException();
        }

        StockLedgerProcessingError result = mapEntityData(resultEntity);
        return result;
    }

    public void delete(StockLedgerProcessingError processingError) throws EntityDeletionFailedException {
        Map<String, Object> keys = new HashMap<>();
        keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER, processingError.getMaterialDocumentNumber());
        keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR, processingError.getMaterialDocumentYear());
        keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM, processingError.getMaterialDocumentItem());
        keys.put(IStockLedgerProcessingError.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER, processingError.getExciseDutyPositionNumber());

        try {
            handler.executeDelete(IExciseDutyEntities.MODEL_NAME + "." + IStockLedgerProcessingError.NAME, keys);
        } catch (Exception e) {
            throw new EntityDeletionFailedException();
        }
    }

    public void upsert(StockLedgerProcessingError processingError) {
        EntityData entityData = mapPojo(processingError);

        try {
            handler.executeInsert(entityData, false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (e.getMessage().contains("[301]: unique constraint violated")) {
                Map<String, Object> keys = new HashMap<>();
                keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER, processingError.getMaterialDocumentNumber());
                keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR, processingError.getMaterialDocumentYear());
                keys.put(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM, processingError.getMaterialDocumentItem());
                keys.put(IStockLedgerProcessingError.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER, processingError.getExciseDutyPositionNumber());

                try {
                    handler.executeUpdate(entityData, keys, false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

    protected EntityData mapPojo(StockLedgerProcessingError processingError) {
        EntityDataBuilder builder = EntityData.getBuilder();
        builder.addKeyElement(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER, processingError.getMaterialDocumentNumber())
                .addKeyElement(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR, processingError.getMaterialDocumentYear())
                .addKeyElement(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM, processingError.getMaterialDocumentItem())
                .addKeyElement(IStockLedgerProcessingError.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER, processingError.getExciseDutyPositionNumber())
                .addElement(IStockLedgerProcessingError.ELEMENT_COMPANY_CODE, processingError.getCompanyCode())
                .addElement(IStockLedgerProcessingError.ELEMENT_PLANT, processingError.getPlant())
                .addElement(IStockLedgerProcessingError.ELEMENT_STORAGE_LOCATION, processingError.getStorageLocation())
                .addElement(IStockLedgerProcessingError.ELEMENT_MATERIAL_DOCUMENT_POSTING_DATE, java.sql.Date.valueOf(processingError.getMaterialDocumentPostingDate()))
                .addElement(IStockLedgerProcessingError.ELEMENT_MATERIAL_NUMBER, processingError.getMaterialNumber())
                .addElement(IStockLedgerProcessingError.ELEMENT_ERROR_TEXT, processingError.getErrorText());

        EntityData entity = builder.buildEntityData(IExciseDutyEntities.MODEL_NAME + "." + IStockLedgerProcessingError.NAME);

        return entity;
    }

    protected StockLedgerProcessingError mapEntityData(EntityData entityData) {
        StockLedgerProcessingError result = new StockLedgerProcessingError();
        result.setMaterialDocumentNumber((String) entityData.getElementValue(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_NUMBER));
        result.setMaterialDocumentYear((String) entityData.getElementValue(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_YEAR));
        result.setMaterialDocumentItem((String) entityData.getElementValue(IStockLedgerProcessingError.KEY_ELEMENT_MATERIAL_DOCUMENT_ITEM));
        result.setExciseDutyPositionNumber((Integer) entityData.getElementValue(IStockLedgerProcessingError.KEY_ELEMENT_EXCISE_DUTY_POSITION_NUMBER));
        result.setCompanyCode((String) entityData.getElementValue(IStockLedgerProcessingError.ELEMENT_COMPANY_CODE));
        result.setPlant((String) entityData.getElementValue(IStockLedgerProcessingError.ELEMENT_PLANT));
        result.setStorageLocation((String) entityData.getElementValue(IStockLedgerProcessingError.ELEMENT_STORAGE_LOCATION));
        result.setMaterialDocumentPostingDate(((Date) entityData.getElementValue(IStockLedgerProcessingError.ELEMENT_MATERIAL_DOCUMENT_POSTING_DATE)).toLocalDate());
        result.setMaterialNumber((String) entityData.getElementValue(IStockLedgerProcessingError.ELEMENT_MATERIAL_NUMBER));
        result.setErrorText((String) entityData.getElementValue(IStockLedgerProcessingError.ELEMENT_ERROR_TEXT));

        return result;
    }
}
