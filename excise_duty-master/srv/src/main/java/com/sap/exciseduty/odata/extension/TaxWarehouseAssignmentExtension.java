package com.sap.exciseduty.odata.extension;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.operations.Create;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponse;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.ITaxWarehouse;
import com.sap.exciseduty.entities.ITaxWarehouseAssignment;

public class TaxWarehouseAssignmentExtension {

    @Create(entity = ITaxWarehouseAssignment.NAME, serviceName = IExciseDutyEntities.TAX_WAREHOUSE_SERVICE)
    public CreateResponse create(CreateRequest createRequest, ExtensionHelper extensionHelper) throws Exception {

        EntityData entity = createRequest.getData();

        if (createRequest.getSourceEntityName().equals(ITaxWarehouse.NAME)) {
            // Create via association from TaxWarehouse
            String exciseDutyTypeId = createRequest.getSourceKeys().get(ITaxWarehouse.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID).toString();
            String taxWarehouseRegistration = createRequest.getSourceKeys().get(ITaxWarehouse.KEY_ELEMENT_TAX_WAREHOUSE_REGISTRATION).toString();
            entity = EntityData.getBuilder(entity)
                    .addKeyElement(ITaxWarehouseAssignment.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID, exciseDutyTypeId)
                    .addElement(ITaxWarehouseAssignment.ELEMENT_TAX_WAREHOUSE_REGISTRATION, taxWarehouseRegistration)
                    .buildEntityData(createRequest.getEntityName());
        }

        DataSourceHandler handler = extensionHelper.getHandler();
        EntityData result = handler.executeInsert(entity, true);

        return CreateResponse.setSuccess().setData(result).response();
    }

}
