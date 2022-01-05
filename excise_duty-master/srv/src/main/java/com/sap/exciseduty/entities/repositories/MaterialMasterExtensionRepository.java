package com.sap.exciseduty.entities.repositories;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.sdk.hana.connectivity.cds.CDSException;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSQuery;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSSelectQueryBuilder;
import com.sap.cloud.sdk.hana.connectivity.cds.ConditionBuilder;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.rt.cds.CDSHandler;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IMaterialMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.MaterialMasterExtension;

public class MaterialMasterExtensionRepository {

    private DataSourceHandler handler;

    public MaterialMasterExtensionRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public List<MaterialMasterExtension> getByKey(String materialNumber, String companyCode) {

        ArrayList<MaterialMasterExtension> result = new ArrayList<>();

        CDSHandler cdsHanlder = (CDSHandler) handler;

        CDSQuery cdsQuery = new CDSSelectQueryBuilder(IExciseDutyEntities.MODEL_NAME + "." + IMaterialMasterExtension.NAME)
                .where(new ConditionBuilder().columnName(IMaterialMasterExtension.KEY_ELEMENT_MATERIAL_NUMBER).EQ(materialNumber)
                        .AND(new ConditionBuilder().columnName(IMaterialMasterExtension.KEY_ELEMENT_COMPANY_CODE).EQ(companyCode)))
                .build();

        try {
            List<EntityData> queryResults = cdsHanlder.executeQuery(cdsQuery).getResult();

            for (int i = 0; i < queryResults.size(); i++) {
                result.add(mapEntity(queryResults.get(i)));
            }
        } catch (CDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    protected MaterialMasterExtension mapEntity(EntityData entity) {
        MaterialMasterExtension result = new MaterialMasterExtension();
        Object value;

        result.setMaterialNumber((String) entity.getElementValue(IMaterialMasterExtension.KEY_ELEMENT_MATERIAL_NUMBER));
        result.setCompanyCode((String) entity.getElementValue(IMaterialMasterExtension.KEY_ELEMENT_COMPANY_CODE));
        result.setExciseDutyTypeId((String) entity.getElementValue(IMaterialMasterExtension.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID));
        result.setExciseDutyProcurementIndicator((String) entity.getElementValue(IMaterialMasterExtension.ELEMENT_EXCISE_DUTY_PROCUREMENT_INDICATOR));
        result.setTaxWarehouseRegistrationOfManufacturingPlant((String) entity.getElementValue(IMaterialMasterExtension.ELEMENT_TAX_WAREHOUSE_REGISTRATION_MANUFACTORING_PLANT));
        result.setExciseDutyNumberForTaxWarehouse((String) entity.getElementValue(IMaterialMasterExtension.ELEMENT_EXCISE_DUTY_NUMBER_FOR_TAX_WAREHOUSE));
        result.setExciseDutyTypeIndependentMaterialGroup((String) entity.getElementValue(IMaterialMasterExtension.ELEMENT_EXCISE_DUTY_TYPE_INDEPENDENT_MATERIAL_GROUP));
        value = entity.getElementValue(IMaterialMasterExtension.ELEMENT_ALKOHOLOC_STRENGTH);
        if (value != null) {
            result.setAlcoholicStrength((BigDecimal) value);
        }
        value = entity.getElementValue(IMaterialMasterExtension.ELEMENT_VALID_FROM);
        if (value != null) {
            result.setValidFrom(((Date) value).toLocalDate());
        }

        return result;
    }

}
