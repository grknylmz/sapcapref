package com.sap.exciseduty.entities.repositories;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.hana.connectivity.cds.CDSQuery;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSSelectQueryBuilder;
import com.sap.cloud.sdk.hana.connectivity.cds.ConditionBuilder;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.rt.cds.CDSHandler;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.ITaxRate;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.TaxRate;

public class TaxRateRepository {

    private DataSourceHandler handler;

    public TaxRateRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public TaxRate getByKeyAndMatchingAlcoholicStrength(String country, String exciseDutyTypeId, String baseQuantityUnit, LocalDate validFrom, BigDecimal alcoholicStength) throws EntityNotFoundException {
        // TODO: Validate that the result can only be 1 entry
        CDSHandler cdsHandler = (CDSHandler) handler;

        CDSQuery cdsQuery = new CDSSelectQueryBuilder(IExciseDutyEntities.MODEL_NAME + "." + ITaxRate.NAME)
                .where(new ConditionBuilder().columnName(ITaxRate.KEY_ELEMENT_COUNTRY).EQ(country)
                        .AND(new ConditionBuilder().columnName(ITaxRate.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID).EQ(exciseDutyTypeId))
                        .AND(new ConditionBuilder().columnName(ITaxRate.KEY_ELEMENT_BASE_QUANTITY_UNIT).EQ(baseQuantityUnit))
                        .AND(new ConditionBuilder().columnName(ITaxRate.KEY_ELEMENT_VALID_FROM).LE(java.sql.Date.valueOf(validFrom)))
                        .AND(new ConditionBuilder().columnName(ITaxRate.KEY_ELEMENT_ALCOHOLIC_STRENGTH_LOWER_LIMIT).LE(alcoholicStength)))
                .orderBy(ITaxRate.KEY_ELEMENT_ALCOHOLIC_STRENGTH_LOWER_LIMIT, true)
                .build();
        List<EntityData> resultEntities;
        try {
            resultEntities = cdsHandler.executeQuery(cdsQuery).getResult();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
        if (resultEntities == null || resultEntities.size() == 0) {
            throw new EntityNotFoundException();
        }
        TaxRate result = mapEntityData(resultEntities.get(0));
        return result;
    }

    protected TaxRate mapEntityData(EntityData entity) {
        TaxRate result = new TaxRate();
        Object value = null;

        result.setCountry((String) entity.getElementValue(ITaxRate.KEY_ELEMENT_COUNTRY));
        result.setExciseDutyTypeId((String) entity.getElementValue(ITaxRate.KEY_ELEMENT_EXCISE_DUTY_TYPE_ID));
        result.setBaseQuantityUnit((String) entity.getElementValue(ITaxRate.KEY_ELEMENT_BASE_QUANTITY_UNIT));
        result.setValidFrom(((Date) entity.getElementValue(ITaxRate.KEY_ELEMENT_VALID_FROM)).toLocalDate());
        value = entity.getElementValue(ITaxRate.KEY_ELEMENT_ALCOHOLIC_STRENGTH_LOWER_LIMIT);

        if (value != null) {
            result.setAlcoholicStrengthLowerLimit((BigDecimal) value);
        }

        result.setTaxRate((BigDecimal) entity.getElementValue(ITaxRate.ELEMENT_TAX_RATE));
        result.setCurrency((String) entity.getElementValue(ITaxRate.ELEMENT_CURRENCY));

        return result;
    }

}
