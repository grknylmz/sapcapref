package com.sap.exciseduty.odata.extension.validations;

import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.MessageContainer;
import com.sap.cloud.sdk.service.prov.api.annotations.BeforeCreate;
import com.sap.cloud.sdk.service.prov.api.annotations.BeforeUpdate;
import com.sap.cloud.sdk.service.prov.api.exits.BeforeCreateResponse;
import com.sap.cloud.sdk.service.prov.api.exits.BeforeUpdateResponse;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.request.UpdateRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.IExciseDutyType;
import com.sap.gateway.core.api.enums.HttpStatus;

public class ExciseDutyTypeValidation {

    @BeforeCreate(entitySet = { IExciseDutyType.NAME }, serviceName = IExciseDutyEntities.EXCISE_DUTY_TYPE_SERVICE)
    public BeforeCreateResponse beforeCreate(CreateRequest req, ExtensionHelper eh) {
        boolean error = validate(req.getData(), req.getMessageContainer());

        if (error) {
            return BeforeCreateResponse
                    .setError(ErrorResponse.getBuilder().setMessage("validations.ExciseDutyTypeValidation")
                            .addContainerMessages()
                            .setStatusCode(HttpStatus.CONFLICT.getStatusCode())
                            .response());
        } else {
            return BeforeCreateResponse.setSuccess().response();
        }
    }

    @BeforeUpdate(entitySet = { IExciseDutyType.NAME }, serviceName = IExciseDutyEntities.EXCISE_DUTY_TYPE_SERVICE)
    public BeforeUpdateResponse beforeUpdate(UpdateRequest req, ExtensionHelper eh) {
        boolean error = validate(req.getData(), req.getMessageContainer());
        if (error) {
            return BeforeUpdateResponse
                    .setError(ErrorResponse.getBuilder().setMessage("validations.ExciseDutyTypeValidation")
                            .addContainerMessages()
                            .setStatusCode(HttpStatus.CONFLICT.getStatusCode())
                            .response());
        } else {
            return BeforeUpdateResponse.setSuccess().response();
        }
    }

    protected boolean validate(EntityData data, MessageContainer messageContainer) {
        boolean error = false;

        /**
         * Validate Decimal Places of element "containerContentDecimalPlaces"
         */
        try {
            int decimalPlaces = (int) data.getElementValue(IExciseDutyType.ELEMENT_CONTAINER_CONTENT_DECIMAL_PLACES);
            if (decimalPlaces > 9 || decimalPlaces < 0) {
                messageContainer.addErrorMessage("validations.ExciseDutyTypeValidation.DecimalPlacesNotInRange", "ExciseDutyType");
                error = true;
            }
        } catch (ClassCastException e) {
            messageContainer.addErrorMessage("validations.ExciseDutyTypeValidation.NotANumber", "ExciseDutyType");
            error = true;
        } catch (NullPointerException e) {
            // Do nothing, as it is not relevant
        }

        /**
         * Validate possible values of element "containerContentCalculationIndicator"
         */
        try {
            int calculationIndicator = (int) data.getElementValue(IExciseDutyType.ELEMENT_CONTAINER_CONTENT_CALCULATION_INDICATOR);
            if (calculationIndicator < 0 || calculationIndicator > 2) {
                messageContainer.addErrorMessage("validations.ExciseDutyTypeValidation.CalculationIndicator.ValueOutOfRange", "ExciseDutyType");
                error = true;
            }

        } catch (ClassCastException e) {
            messageContainer.addErrorMessage("validations.ExciseDutyTypeValidation.CalculationIndicator.NotANumber", "ExciseDutyType");
            error = true;
        } catch (NullPointerException e) {
            // Do nothing, as it is not relevant
        }

        return error;
    }

}
