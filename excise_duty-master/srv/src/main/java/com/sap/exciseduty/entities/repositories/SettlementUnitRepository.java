package com.sap.exciseduty.entities.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.ISettlementUnit;
import com.sap.exciseduty.entities.repositories.pojos.SettlementUnit;

public class SettlementUnitRepository {

    private DataSourceHandler handler;

    public SettlementUnitRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public SettlementUnit getByKey(String companyCode, String exciseDutyTypeId) {
        Map<String, Object> keys = new HashMap<>();
        keys.put(ISettlementUnit.KEY_ELEMENT_COMPANY_CODE, companyCode);
        keys.put(ISettlementUnit.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID, exciseDutyTypeId);

        ArrayList<String> flattenedElementNames = new ArrayList<>();
        flattenedElementNames.add(ISettlementUnit.KEY_ELEMENT_COMPANY_CODE);
        flattenedElementNames.add(ISettlementUnit.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID);
        flattenedElementNames.add(ISettlementUnit.ELEMENT_SETTLEMENT_UNIT);
        flattenedElementNames.add(ISettlementUnit.ELEMENT_BASE_QUANTITY_UNIT);
        flattenedElementNames.add(ISettlementUnit.ELEMENT_VOLUME_DECIMAL_PLACES);

        try {
            EntityData resultEntity = handler.executeRead(IExciseDutyEntities.MODEL_NAME + "." + ISettlementUnit.NAME, keys, flattenedElementNames);
            SettlementUnit result = mapEntityData(resultEntity);
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    protected SettlementUnit mapEntityData(EntityData entity) {
        SettlementUnit result = new SettlementUnit();

        result.setCompanyCode((String) entity.getElementValue(ISettlementUnit.KEY_ELEMENT_COMPANY_CODE));
        result.setExciseDutyTypeId((String) entity.getElementValue(ISettlementUnit.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID));
        result.setSettlementUnit((String) entity.getElementValue(ISettlementUnit.ELEMENT_SETTLEMENT_UNIT));
        result.setBaseQuantityUnit((String) entity.getElementValue(ISettlementUnit.ELEMENT_BASE_QUANTITY_UNIT));
        result.setVolumeDecimalPlaces((Integer) entity.getElementValue(ISettlementUnit.ELEMENT_VOLUME_DECIMAL_PLACES));

        return result;
    }

}
