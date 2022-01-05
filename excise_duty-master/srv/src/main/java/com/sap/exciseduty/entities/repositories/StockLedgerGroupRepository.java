package com.sap.exciseduty.entities.repositories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IStockLedgerGroup;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerGroup;

public class StockLedgerGroupRepository {

    private DataSourceHandler handler;

    public StockLedgerGroupRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public StockLedgerGroup getByKey(String id) {
        Map<String, Object> keys = new HashMap<>();
        keys.put(IStockLedgerGroup.KEY_ELEMENT_ID, id);

        List<String> flattenedElementNames = Arrays.asList(
                IStockLedgerGroup.KEY_ELEMENT_ID,
                IStockLedgerGroup.ELEMENT_DESCRIPTION,
                IStockLedgerGroup.ELEMENT_STOCK_LEDGER_DIVISION,
                IStockLedgerGroup.ELEMENT_STOCK_LEDGER_SUBDIVISION,
                IStockLedgerGroup.ELEMENT_MOVEMENT_ENTRY_BEHAVIOR);

        EntityData resultEntity;
        try {
            resultEntity = handler.executeRead(IExciseDutyEntities.MODEL_NAME + "." + IStockLedgerGroup.NAME, keys, flattenedElementNames);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }

        StockLedgerGroup result = mapCategoryEntityData(resultEntity);
        return result;
    }

    protected StockLedgerGroup mapCategoryEntityData(EntityData entity) {
        StockLedgerGroup result = new StockLedgerGroup();
        result.setId((String) entity.getElementValue(IStockLedgerGroup.KEY_ELEMENT_ID));
        result.setDescription((String) entity.getElementValue(IStockLedgerGroup.ELEMENT_DESCRIPTION));

        result.setStockLedgerDivision((String) entity.getElementValue(IStockLedgerGroup.ELEMENT_STOCK_LEDGER_DIVISION));
        result.setStockLedgerSubdivision((String) entity.getElementValue(IStockLedgerGroup.ELEMENT_STOCK_LEDGER_SUBDIVISION));
        result.setMovementEntryBehavior((String) entity.getElementValue(IStockLedgerGroup.ELEMENT_MOVEMENT_ENTRY_BEHAVIOR));

        return result;
    }

}
