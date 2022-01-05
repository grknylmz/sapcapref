package com.sap.exciseduty.odata.extension;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.operations.Create;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponse;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IMovementCategory;
import com.sap.exciseduty.entities.IMovementTypeMapping;

public class MovementTypeMappingExtension {

    @Create(entity = IMovementTypeMapping.NAME, serviceName = IExciseDutyEntities.MOVEMENT_CATEGORY_SERVICE)
    public CreateResponse create(CreateRequest createRequest, ExtensionHelper extensionHelper) throws Exception {

        EntityData entity = createRequest.getData();

        if (createRequest.getSourceEntityName().equals(IMovementCategory.NAME)) {
            // Create via association from MovementCategory
            String movementCategoryId = createRequest.getSourceKeys().get(IMovementCategory.KEY_ELEMENT_ID).toString();
            entity = EntityData.getBuilder(entity).addElement(IMovementTypeMapping.ELEMENT_MOVEMENT_CATEGORY_ID, movementCategoryId).buildEntityData(createRequest.getEntityName());
        }

        DataSourceHandler handler = extensionHelper.getHandler();
        EntityData result = handler.executeInsert(entity, true);

        return CreateResponse.setSuccess().setData(result).response();
    }
}
