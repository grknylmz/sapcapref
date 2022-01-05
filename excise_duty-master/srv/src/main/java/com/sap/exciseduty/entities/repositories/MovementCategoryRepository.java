package com.sap.exciseduty.entities.repositories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IMovementCategory;
import com.sap.exciseduty.entities.IMovementTypeMapping;
import com.sap.exciseduty.entities.repositories.pojos.MovementCategory;
import com.sap.exciseduty.entities.repositories.pojos.MovementTypeMapping;

public class MovementCategoryRepository {

    private DataSourceHandler handler;

    public MovementCategoryRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public MovementCategory getByKey(String id) {
        Map<String, Object> keys = new HashMap<>();
        keys.put(IMovementCategory.KEY_ELEMENT_ID, id);

        List<String> flattenedElementNames = Arrays.asList(
                IMovementCategory.KEY_ELEMENT_ID,
                IMovementCategory.ELEMENT_DESCRIPTION);

        EntityData resultEntity;
        try {
            resultEntity = handler.executeRead(IExciseDutyEntities.MOVEMENT_CATEGORY_SERVICE + "." + IMovementCategory.NAME, keys, flattenedElementNames);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }

        MovementCategory result = mapCategoryEntityData(resultEntity);
        return result;
    }

    public MovementTypeMapping getMovementTypeMappingByKey(String erpMovementType, String erpMovementIndicator) {
        Map<String, Object> keys = new HashMap<>();
        keys.put(IMovementTypeMapping.KEY_ELEMENT_ERP_MOVEMENT_TYPE, erpMovementType);
        keys.put(IMovementTypeMapping.KEY_ELEMENT_ERP_MOVEMENT_INDICATOR, erpMovementIndicator);

        List<String> flattenedElementNames = Arrays.asList(
                IMovementTypeMapping.KEY_ELEMENT_ERP_MOVEMENT_TYPE,
                IMovementTypeMapping.KEY_ELEMENT_ERP_MOVEMENT_INDICATOR,
                IMovementTypeMapping.ELEMENT_MOVEMENT_CATEGORY_ID);

        EntityData resultEntity;
        try {
            resultEntity = handler.executeRead(IExciseDutyEntities.MOVEMENT_CATEGORY_SERVICE + "." + IMovementTypeMapping.NAME, keys, flattenedElementNames);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }

        MovementTypeMapping result = mapTypeMappingEntityData(resultEntity);
        return result;
    }

    protected MovementCategory mapCategoryEntityData(EntityData entity) {
        MovementCategory result = new MovementCategory();
        result.setId((String) entity.getElementValue(IMovementCategory.KEY_ELEMENT_ID));
        result.setDescription((String) entity.getElementValue(IMovementCategory.ELEMENT_DESCRIPTION));

        return result;
    }

    protected MovementTypeMapping mapTypeMappingEntityData(EntityData entity) {
        MovementTypeMapping result = new MovementTypeMapping();
        result.setErpMovementType((String) entity.getElementValue(IMovementTypeMapping.KEY_ELEMENT_ERP_MOVEMENT_TYPE));
        result.setErpMovementIndicator((String) entity.getElementValue(IMovementTypeMapping.KEY_ELEMENT_ERP_MOVEMENT_INDICATOR));
        result.setEdMovementCategoryId((String) entity.getElementValue(IMovementTypeMapping.ELEMENT_MOVEMENT_CATEGORY_ID));

        return result;
    }

}
