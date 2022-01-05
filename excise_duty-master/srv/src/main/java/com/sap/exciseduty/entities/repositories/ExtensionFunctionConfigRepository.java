package com.sap.exciseduty.entities.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IExtensionFunctionConfig;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.ExtensionFunctionConfig;

public class ExtensionFunctionConfigRepository {

    private DataSourceHandler handler;

    public ExtensionFunctionConfigRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public ExtensionFunctionConfig getByKey(String exciseDutyTypeId, String extensionPoint) throws EntityNotFoundException {
        Map<String, Object> keys = new HashMap<>();
        keys.put(IExtensionFunctionConfig.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID, exciseDutyTypeId);
        keys.put(IExtensionFunctionConfig.KEY_ELEMENT_EXTENSION_POINT, extensionPoint);

        ArrayList<String> flattenedElementNames = new ArrayList<>();
        flattenedElementNames.add(IExtensionFunctionConfig.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID);
        flattenedElementNames.add(IExtensionFunctionConfig.KEY_ELEMENT_EXTENSION_POINT);
        flattenedElementNames.add(IExtensionFunctionConfig.ELEMENT_EXTENSION_DESTINATION);

        EntityData resultEntity;
        try {
            resultEntity = handler.executeRead(IExciseDutyEntities.MODEL_NAME + "." + IExtensionFunctionConfig.NAME, keys, flattenedElementNames);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }

        if (resultEntity == null) {
            throw new EntityNotFoundException();
        }

        ExtensionFunctionConfig result = mapEntityData(resultEntity);
        return result;
    }

    protected ExtensionFunctionConfig mapEntityData(EntityData entity) {
        ExtensionFunctionConfig result = new ExtensionFunctionConfig();

        result.setExciseDutyTypeId((String) entity.getElementValue(IExtensionFunctionConfig.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID));
        result.setExtensionPoint((String) entity.getElementValue(IExtensionFunctionConfig.KEY_ELEMENT_EXTENSION_POINT));
        result.setExtensionDestination((String) entity.getElementValue(IExtensionFunctionConfig.ELEMENT_EXTENSION_DESTINATION));

        return result;
    }
}
