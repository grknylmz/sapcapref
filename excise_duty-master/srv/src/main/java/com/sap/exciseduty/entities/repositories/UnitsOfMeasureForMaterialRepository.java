package com.sap.exciseduty.entities.repositories;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.hana.connectivity.cds.CDSQuery;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSSelectQueryBuilder;
import com.sap.cloud.sdk.hana.connectivity.cds.ConditionBuilder;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.rt.cds.CDSHandler;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IUnitsOfMeasureForMaterial;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.UnitsOfMeasureForMaterial;

public class UnitsOfMeasureForMaterialRepository {

    private DataSourceHandler handler;

    public UnitsOfMeasureForMaterialRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public UnitsOfMeasureForMaterial getByAlternativeUnitOfMeasure(String materialNumber, String alternativeUnitOfMeasure) throws EntityNotFoundException {

        CDSHandler cdsHandler = (CDSHandler) handler;

        CDSQuery cdsQuery = new CDSSelectQueryBuilder(IExciseDutyEntities.MASTER_DATA_REPLICATION_MODEL + "." + IUnitsOfMeasureForMaterial.NAME)
                .where(new ConditionBuilder().columnName(IUnitsOfMeasureForMaterial.KEY_ELEMENT_MATERIAL_NUMBER).EQ(materialNumber)
                        .AND(new ConditionBuilder().columnName(IUnitsOfMeasureForMaterial.KEY_ELEMENT_ALTERNATIVE_UNIT_OF_MEASURE).EQ(alternativeUnitOfMeasure)))
                .build();
        List<EntityData> resultEntities;
        try {
            resultEntities = cdsHandler.executeQuery(cdsQuery).getResult();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
        if (resultEntities == null) {
            throw new EntityNotFoundException();
        }
        UnitsOfMeasureForMaterial result = mapEntityData(resultEntities.get(0));
        return result;
    }

    protected UnitsOfMeasureForMaterial mapEntityData(EntityData entity) {
        UnitsOfMeasureForMaterial result = new UnitsOfMeasureForMaterial();
        result.setMaterialNumber((String) entity.getElementValue(IUnitsOfMeasureForMaterial.KEY_ELEMENT_MATERIAL_NUMBER));
        result.setAlternativeUnitOfMeasure((String) entity.getElementValue(IUnitsOfMeasureForMaterial.KEY_ELEMENT_ALTERNATIVE_UNIT_OF_MEASURE));
        result.setNumeratorForConversionToBaseUnitsOfMeasure((BigDecimal) entity.getElementValue(IUnitsOfMeasureForMaterial.ELEMENT_NUMERATOR_FOR_CONVERSION_TO_BASE_UNITS_OF_MEASURE));
        result.setDenominatorForConversionToBaseUnitsOfMeasure((BigDecimal) entity.getElementValue(IUnitsOfMeasureForMaterial.ELEMENT_DENOMINATOR_FOR_CONVERSION_TO_BASE_UNITS_OF_MEASURE));

        return result;
    }

}
