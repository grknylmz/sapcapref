package com.sap.exciseduty.odata.actions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.Action;
import com.sap.cloud.sdk.service.prov.api.request.OperationRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.OperationResponse;
import com.sap.exciseduty.entities.repositories.MaterialMasterExtensionRepository;
import com.sap.exciseduty.entities.repositories.SettlementUnitRepository;
import com.sap.exciseduty.entities.repositories.TaxWarehouseRepository;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.MaterialMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.SettlementUnit;
import com.sap.exciseduty.entities.repositories.pojos.TaxWarehouse;
import com.sap.exciseduty.entities.repositories.pojos.TaxWarehouseAssignment;
import com.sap.exciseduty.processors.TaxCalculationProcessor;
import com.sap.exciseduty.processors.exceptions.ConvertionToSettlementUnitNotPossibleException;
import com.sap.exciseduty.processors.exceptions.TaxRateNotFoundException;
import com.sap.exciseduty.processors.pojos.TaxCalculationInput;
import com.sap.exciseduty.processors.pojos.TaxCalculationResult;
import com.sap.exciseduty.utility.EntityHelper;
import com.sap.exciseduty.utility.WebSecurityConfig;
import com.sap.xs2.security.container.SecurityContext;
import com.sap.xs2.security.container.UserInfoException;

public class ExciseDutyCalculationAction {

    @Action(Name = "ExciseDutyCalculation", serviceName = "StockLedgerService")
    public OperationResponse execute(OperationRequest functionRequest, ExtensionHelper extensionHelper) {

        // Verify Scope of incoming Request
        try {
            if (!SecurityContext.getUserInfo().checkLocalScope(WebSecurityConfig.TAXCALC_SCOPE)) {
                return OperationResponse.setError((ErrorResponse.getBuilder().setMessage("Forbidden").setStatusCode(HttpStatus.SC_FORBIDDEN)).response());
            }
        } catch (UserInfoException e1) {
            return OperationResponse.setError((ErrorResponse.getBuilder().setMessage("Internal Server Error").setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)).response());
        }

        TaxCalculationProcessor taxProcessor = new TaxCalculationProcessor(extensionHelper.getHandler());
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> parameters = functionRequest.getParameters();
        // TODO: Handle Customer Number, not relevant yet
        TaxCalculationInput input = new TaxCalculationInput();
        String companyCode = (String) parameters.get("companyCode");
        String plant = (String) parameters.get("plant");
        String materialNumber = (String) parameters.get("materialNumber");
        LocalDate postingDate = EntityHelper.convertDate((String) parameters.get("postingDate"));

        input.setMaterialNumber(materialNumber);
        input.setMaterialDocumentPostingDate(postingDate);
        input.setMaterialDocumentQuantity(new BigDecimal((String) parameters.get("quantity")));
        input.setMaterialDocumentBaseUnitOfMeasure((String) parameters.get("baseUnitOfMeasure"));

        List<MaterialMasterExtension> materialMasterExtensions = getMaterialMasterExtension(materialNumber, companyCode, extensionHelper.getHandler());

        if (materialMasterExtensions == null || materialMasterExtensions.size() == 0) {
            return OperationResponse.setSuccess().response();
        }

        for (MaterialMasterExtension materialMasterExtension : materialMasterExtensions) {
            // Check if material location is relevant for excise duty
            try {
                @SuppressWarnings("unused")
                TaxWarehouse taxWarehouse = getTaxWarehouseRegistration(plant, materialMasterExtension.getExciseDutyTypeId(), postingDate, extensionHelper.getHandler());
            } catch (EntityNotFoundException e) {
                // Not relevant for excise duty
                continue;
            }
            SettlementUnit settlementUnit = getSettlementUnit(companyCode, materialMasterExtension.getExciseDutyTypeId(), extensionHelper.getHandler());
            input.setMaterialMasterExtension(materialMasterExtension);
            input.setSettlementUnit(settlementUnit);

            TaxCalculationResult taxCalculationResult;
            try {
                taxCalculationResult = taxProcessor.execute(input);
            } catch (TaxRateNotFoundException | ConvertionToSettlementUnitNotPossibleException e) {
                return OperationResponse.setError(ErrorResponse.getBuilder().setMessage("Excise Duty Configuration data not maintained").setStatusCode(406).response());
            }

            Map<String, Object> resultEntity = new HashMap<>();
            resultEntity.put("exciseDutyTypeId", materialMasterExtension.getExciseDutyTypeId());
            resultEntity.put("taxValueAmount", taxCalculationResult.getTaxValueAmount());
            resultEntity.put("taxValueCurrency", taxCalculationResult.getTaxValueCurrency());

            result.add(resultEntity);
        }

        return OperationResponse.setSuccess().setComplexData(result).response();
    }

    protected TaxWarehouse getTaxWarehouseRegistration(String plant, String excuseDutyTyeID, LocalDate postingDate, DataSourceHandler handler) throws EntityNotFoundException {
        TaxWarehouseRepository repo = new TaxWarehouseRepository(handler);

        List<TaxWarehouseAssignment> assigment = repo.getAssignments(plant, excuseDutyTyeID, postingDate);
        TaxWarehouse result = repo.getByKey(excuseDutyTyeID, assigment.get(0).getTaxWarehouseRegistration());

        return result;
    }

    protected List<MaterialMasterExtension> getMaterialMasterExtension(String materialNumber, String companyCode, DataSourceHandler handler) {
        MaterialMasterExtensionRepository repo = new MaterialMasterExtensionRepository(handler);

        List<MaterialMasterExtension> result = repo.getByKey(materialNumber, companyCode);

        return result;
    }

    protected SettlementUnit getSettlementUnit(String companyCode, String exciseDutyTypeId, DataSourceHandler handler) {
        SettlementUnitRepository repo = new SettlementUnitRepository(handler);
        SettlementUnit result = repo.getByKey(companyCode, exciseDutyTypeId);
        return result;
    }

}
