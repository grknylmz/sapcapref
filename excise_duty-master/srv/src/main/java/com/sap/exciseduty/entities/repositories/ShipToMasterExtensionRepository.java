package com.sap.exciseduty.entities.repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.hana.connectivity.cds.CDSException;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSQuery;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSSelectQueryBuilder;
import com.sap.cloud.sdk.hana.connectivity.cds.ConditionBuilder;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.rt.cds.CDSHandler;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IShipToMasterExtension;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.ShipToMasterExtension;
import com.sap.exciseduty.utility.HANAHelper;

public class ShipToMasterExtensionRepository {
    private DataSourceHandler handler;

    public ShipToMasterExtensionRepository(DataSourceHandler handler) {
        this.handler = handler;
    }

    public ShipToMasterExtension queryByKey(String customerNumber, LocalDate date) throws EntityNotFoundException {
        // TODO: Validate that the result can only be 1 entry
        CDSHandler cdsHandler = (CDSHandler) handler;

        CDSQuery cdsQuery = new CDSSelectQueryBuilder(IExciseDutyEntities.MODEL_NAME + "." + IShipToMasterExtension.NAME)
                .where(new ConditionBuilder().columnName(IShipToMasterExtension.KEY_ELEMENT_CUSTOMER_NUMER).EQ(customerNumber)
                        .AND(new ConditionBuilder().columnName(IShipToMasterExtension.KEY_ELEMENT_VALID_FROM).LE(java.sql.Date.valueOf(date)))
                        .AND(new ConditionBuilder().columnName(IShipToMasterExtension.ELEMENT_VALID_TO).ISNULL()
                                .OR(new ConditionBuilder().columnName(IShipToMasterExtension.ELEMENT_VALID_TO).GT(java.sql.Date.valueOf(date)))))
                .build();
        List<EntityData> resultEntities;
        try {
            resultEntities = cdsHandler.executeQuery(cdsQuery).getResult();
        } catch (CDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }

        if (resultEntities == null || resultEntities.size() == 0) {
            throw new EntityNotFoundException();
        }

        ShipToMasterExtension result = mapEntityData(resultEntities.get(0));
        return result;
    }

    protected ShipToMasterExtension mapEntityData(EntityData entity) {
        Object value;

        ShipToMasterExtension result = new ShipToMasterExtension();

        result.setCustomerNumber((String) entity.getElementValue(IShipToMasterExtension.KEY_ELEMENT_CUSTOMER_NUMER));
        value = entity.getElementValue(IShipToMasterExtension.KEY_ELEMENT_VALID_FROM);
        if (value != null) {
            result.setValidFrom(((Date) value).toLocalDate());
        }
        value = entity.getElementValue(IShipToMasterExtension.ELEMENT_VALID_TO);
        if (value != null) {
            result.setValidTo(((Date) value).toLocalDate());
        }

        result.setExternalTaxWarehouseRegistration((String) entity.getElementValue(IShipToMasterExtension.ELEMENT_EXTERNAL_TAX_WAREHOUSE_REGISTRATION));
        result.setExternalExciseDutyNumber((String) entity.getElementValue(IShipToMasterExtension.ELEMENT_EXTERNAL_EXCISE_DUTY_NUMBER));
        result.setExciseDutySpecialPartnerTypeId((String) entity.getElementValue(IShipToMasterExtension.ELEMENT_EXTERNAL_EXCISE_DUTY_SPECIAL_PARTNER_TYPE_ID));

        value = entity.getElementValue(IShipToMasterExtension.ELEMENT_EXTERNAL_THIRD_COUNTRY_INDICATOR);
        if (value != null) {
            result.setThirdCountryIndicator(HANAHelper.convertBooleanValue(value));
        }

        return result;
    }
}
