package com.sap.exciseduty.odata.extension;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.operations.Create;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponse;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IExciseDutyType;
import com.sap.exciseduty.entities.ISettlementUnit;

public class SettlementUnitExtension {

    @Create(entity = ISettlementUnit.NAME, serviceName = IExciseDutyEntities.EXCISE_DUTY_TYPE_SERVICE)
    public CreateResponse create(CreateRequest createRequest, ExtensionHelper extensionHelper) throws Exception {

        EntityData entity = createRequest.getData();

        if (createRequest.getSourceEntityName().equals(IExciseDutyType.NAME)) {
            // Create via association from Excise Duty Type
            String exciseDutyTypeId = createRequest.getSourceKeys().get(IExciseDutyType.ELEMENT_ID).toString();
            entity = EntityData.getBuilder(entity).addKeyElement(ISettlementUnit.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID, exciseDutyTypeId).buildEntityData(createRequest.getEntityName());
        }

        DataSourceHandler handler = extensionHelper.getHandler();
        EntityData result = handler.executeInsert(entity, true);

        return CreateResponse.setSuccess().setData(result).response();
    }
}
