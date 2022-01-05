package com.sap.exciseduty.entities.repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.hana.connectivity.cds.CDSException;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSQuery;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSSelectQueryBuilder;
import com.sap.cloud.sdk.hana.connectivity.cds.ConditionBuilder;
import com.sap.cloud.sdk.hana.connectivity.cds.ConditionBuilder.Condition;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.rt.cds.CDSHandler;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.ITaxWarehouse;
import com.sap.exciseduty.entities.ITaxWarehouseAssignment;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.TaxWarehouse;
import com.sap.exciseduty.entities.repositories.pojos.TaxWarehouseAssignment;
import com.sap.exciseduty.utility.HANAHelper;

public class TaxWarehouseRepository {

    private DataSourceHandler handler;

    public TaxWarehouseRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public TaxWarehouse getByKey(String exciseDutyTypeId, String taxWarehouseRegistration) throws EntityNotFoundException {
        Map<String, Object> keys = new HashMap<>();
        keys.put(ITaxWarehouse.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID, exciseDutyTypeId);
        keys.put(ITaxWarehouse.KEY_ELEMENT_TAX_WAREHOUSE_REGISTRATION, taxWarehouseRegistration);

        ArrayList<String> flattenedElementNames = new ArrayList<>();
        flattenedElementNames.add(ITaxWarehouse.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID);
        flattenedElementNames.add(ITaxWarehouse.KEY_ELEMENT_TAX_WAREHOUSE_REGISTRATION);
        flattenedElementNames.add(ITaxWarehouse.ELEMENT_VALID_FROM);
        flattenedElementNames.add(ITaxWarehouse.ELEMENT_VALID_TO);
        flattenedElementNames.add(ITaxWarehouse.ELEMENT_DESCRIPTION);
        flattenedElementNames.add(ITaxWarehouse.ELEMENT_COMPANY_CODE);
        flattenedElementNames.add(ITaxWarehouse.ELEMENT_USE_STOCK_LEDGER_SUBDIVISINS);
        flattenedElementNames.add(ITaxWarehouse.ELEMENT_STOCK_LEDGER_TYPE);

        EntityData resultEntity;
        try {
            resultEntity = handler.executeRead(IExciseDutyEntities.MODEL_NAME + "." + ITaxWarehouse.NAME, keys, flattenedElementNames);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }

        if (resultEntity == null) {
            throw new EntityNotFoundException();
        }

        TaxWarehouse result = mapEntityData(resultEntity);
        return result;
    }

    public TaxWarehouseAssignment getAssignmentByKey(String plant, String storageLocation, String exciseDutyTyeID, LocalDate date) throws EntityNotFoundException {
        // TODO: Is there only one TaxWarehouseAssignment for a given plant, storageLication, exciseDutyTypeID and valid
        // Date?
        CDSHandler cdsHandler = (CDSHandler) handler;

        Condition condition = new ConditionBuilder().columnName(ITaxWarehouseAssignment.KEY_ELEMENT_PLANT).EQ(plant)
                .AND(new ConditionBuilder().columnName(ITaxWarehouseAssignment.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID).EQ(exciseDutyTyeID))
                .AND(new ConditionBuilder().columnName(ITaxWarehouseAssignment.KEY_ELEMENT_VALID_FROM).LE(java.sql.Date.valueOf(date)))
                .AND(new ConditionBuilder().columnName(ITaxWarehouseAssignment.ELEMENT_TAX_VALID_TO).ISNULL()
                        .OR(new ConditionBuilder().columnName(ITaxWarehouseAssignment.ELEMENT_TAX_VALID_TO).GT(java.sql.Date.valueOf(date))));

        if (storageLocation != null && !storageLocation.isEmpty()) {
            condition = condition.AND(new ConditionBuilder().columnName(ITaxWarehouseAssignment.KEY_ELEMENT_STORAGE_LOCATION).EQ(storageLocation));
        }

        CDSQuery cdsQuery = new CDSSelectQueryBuilder(IExciseDutyEntities.MODEL_NAME + "." + ITaxWarehouseAssignment.NAME)
                .where(condition)
                .build();

        try {
            List<EntityData> queryResult = cdsHandler.executeQuery(cdsQuery).getResult();
            if (queryResult == null || queryResult.size() == 0) {
                throw new EntityNotFoundException();
            }
            return mapAssignemntEntityData(queryResult.get(0));
        } catch (CDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    public List<TaxWarehouseAssignment> getAssignments(String plant, String exciseDutyTyeID, LocalDate date) throws EntityNotFoundException {
        // TODO: Is there only one TaxWarehouseAssignment for a given plant, storageLication, exciseDutyTypeID and valid
        // Date?
        ArrayList<TaxWarehouseAssignment> result = new ArrayList<>();
        CDSHandler cdsHandler = (CDSHandler) handler;

        Condition condition = new ConditionBuilder().columnName(ITaxWarehouseAssignment.KEY_ELEMENT_PLANT).EQ(plant)
                .AND(new ConditionBuilder().columnName(ITaxWarehouseAssignment.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID).EQ(exciseDutyTyeID))
                .AND(new ConditionBuilder().columnName(ITaxWarehouseAssignment.KEY_ELEMENT_VALID_FROM).LE(java.sql.Date.valueOf(date)))
                .AND(new ConditionBuilder().columnName(ITaxWarehouseAssignment.ELEMENT_TAX_VALID_TO).ISNULL()
                        .OR(new ConditionBuilder().columnName(ITaxWarehouseAssignment.ELEMENT_TAX_VALID_TO).GT(java.sql.Date.valueOf(date))));

        CDSQuery cdsQuery = new CDSSelectQueryBuilder(IExciseDutyEntities.MODEL_NAME + "." + ITaxWarehouseAssignment.NAME)
                .where(condition)
                .build();

        try {
            List<EntityData> queryResult = cdsHandler.executeQuery(cdsQuery).getResult();
            if (queryResult == null || queryResult.size() == 0) {
                throw new EntityNotFoundException();
            }
            for (EntityData entityData : queryResult) {
                result.add(mapAssignemntEntityData(entityData));
            }
            return result;
        } catch (CDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    protected TaxWarehouse mapEntityData(EntityData entity) {
        TaxWarehouse result = new TaxWarehouse();
        Object value = null;

        result.setExciseDutyTypeId((String) entity.getElementValue(ITaxWarehouse.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID));
        result.setTaxWarehouseRegistration((String) entity.getElementValue(ITaxWarehouse.KEY_ELEMENT_TAX_WAREHOUSE_REGISTRATION));
        result.setValidFrom(((Date) entity.getElementValue(ITaxWarehouse.ELEMENT_VALID_FROM)).toLocalDate());

        value = entity.getElementValue(ITaxWarehouse.ELEMENT_VALID_TO);
        if (value != null) {
            result.setValidTo(((Date) value).toLocalDate());
        }
        result.setDescription((String) entity.getElementValue(ITaxWarehouse.ELEMENT_DESCRIPTION));
        result.setCompanyCode((String) entity.getElementValue(ITaxWarehouse.ELEMENT_COMPANY_CODE));
        value = entity.getElementValue(ITaxWarehouse.ELEMENT_USE_STOCK_LEDGER_SUBDIVISINS);
        if (value != null) {
            result.setUseStockLedgerSubdivisions(HANAHelper.convertBooleanValue(value));
        }
        result.setStockLedgerType((String) entity.getElementValue(ITaxWarehouse.ELEMENT_STOCK_LEDGER_TYPE));

        return result;
    }

    protected TaxWarehouseAssignment mapAssignemntEntityData(EntityData entity) {
        TaxWarehouseAssignment result = new TaxWarehouseAssignment();
        Object value = null;

        result.setPlant((String) entity.getElementValue(ITaxWarehouseAssignment.KEY_ELEMENT_PLANT));
        result.setStorageLocation((String) entity.getElementValue(ITaxWarehouseAssignment.KEY_ELEMENT_STORAGE_LOCATION));
        result.setExciseDutyTypeId((String) entity.getElementValue(ITaxWarehouseAssignment.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID));
        result.setValidFrom(((Date) entity.getElementValue(ITaxWarehouseAssignment.KEY_ELEMENT_VALID_FROM)).toLocalDate());
        value = entity.getElementValue(ITaxWarehouseAssignment.ELEMENT_TAX_VALID_TO);
        if (value != null) {
            result.setValidTo(((Date) value).toLocalDate());
        }
        result.setTaxWarehouseRegistration((String) entity.getElementValue(ITaxWarehouseAssignment.ELEMENT_TAX_WAREHOUSE_REGISTRATION));

        return result;
    }

}
