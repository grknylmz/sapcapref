package com.sap.exciseduty.odata.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.Function;
import com.sap.cloud.sdk.service.prov.api.request.OperationRequest;
import com.sap.cloud.sdk.service.prov.api.response.OperationResponse;
import com.sap.cloud.sdk.service.prov.rt.cds.CDSHandler;
import com.sap.exciseduty.odata.actions.pojo.TaxReportingActionInput;
import com.sap.exciseduty.odata.actions.pojo.TaxReportingResult;

public class TaxReportingAction {

    @Function(Name = "TaxReporting", serviceName = "StockLedgerService")
    public OperationResponse execute(OperationRequest functionRequest, ExtensionHelper extensionHelper) {
        TaxReportingActionInput input = new TaxReportingActionInput(functionRequest.getParameters());
        List<TaxReportingResult> taxReportinResults = executeInternal(input, extensionHelper.getHandler());

        List<Map<String, Object>> result = new ArrayList<>();
        for (TaxReportingResult taxReportinResult : taxReportinResults) {
            result.add(taxReportinResult.toTypeData());
        }
        return OperationResponse.setSuccess().setComplexData(result).response();
    }

    protected List<TaxReportingResult> executeInternal(TaxReportingActionInput actionInput, DataSourceHandler handler) {
        List<TaxReportingResult> result = new ArrayList<>();
        /*
         * SQL Request blueprint
         */
        // select "alcoholicStrength", "stockLedgerGroupId", "exciseDutyProcurementIndicator", SUM("taxValueAmount")
        // from "DB25385980C941C8A40E7F84BE887AD0"."ExciseDutyModel.StockLedgerLineItem" where "stockLedgerGroupId" IN
        // ('02010', '02030', '02040') and "materialDocumentPostingDate" between '2017-01-01' and '2018-01-31' group by
        // "alcoholicStrength", "stockLedgerGroupId", "exciseDutyProcurementIndicator" order by "alcoholicStrength",
        // "exciseDutyProcurementIndicator", "stockLedgerGroupId"

        CDSHandler cdsHandler = (CDSHandler) handler;
        Connection con = cdsHandler.getConnection();
        try (PreparedStatement prepStmt = con.prepareStatement(
                "select \"alcoholicStrength\", \"exciseDutyProcurementIndicator\", \"stockLedgerGroupId\", SUM(\"taxValueAmount\") from \"ExciseDutyModel.StockLedgerLineItem\" where \"stockLedgerGroupId\" IN (?, ?, ?) and \"materialDocumentPostingDate\" between ? and ? and \"taxWarehouseRegistration\" = ? group by \"alcoholicStrength\", \"exciseDutyProcurementIndicator\", \"stockLedgerGroupId\" order by \"alcoholicStrength\", \"exciseDutyProcurementIndicator\", \"stockLedgerGroupId\"");) {
            prepStmt.setString(1, "02010");
            prepStmt.setString(2, "02030");
            prepStmt.setString(3, "02040");
            prepStmt.setDate(4, java.sql.Date.valueOf(actionInput.getFromDate()));
            prepStmt.setDate(5, java.sql.Date.valueOf(actionInput.getToDate()));
            prepStmt.setString(6, actionInput.getTaxWarehouseRegistration());

            ResultSet resultSet = prepStmt.executeQuery();
            TaxReportingResult reportingResult = null;
            while (resultSet.next()) {
                if (reportingResult == null || !BigDecimal.valueOf(resultSet.getDouble("alcoholicStrength")).equals(reportingResult.getAlcoholicStrength())
                        || (resultSet.getString("exciseDutyProcurementIndicator") != null && !resultSet.getString("exciseDutyProcurementIndicator").equals(reportingResult.getExciseDutyProcurementIndicator()))) {
                    if (reportingResult != null) {
                        result.add(reportingResult);
                    }
                    reportingResult = new TaxReportingResult();
                    reportingResult.setExciseDutyTypeId("BI");
                    reportingResult.setExciseDutyProcurementIndicator(resultSet.getString("exciseDutyProcurementIndicator"));
                    reportingResult.setAlcoholicStrength(BigDecimal.valueOf(resultSet.getDouble("alcoholicStrength")));
                    reportingResult.setTaxValueCurrency("EUR");
                }

                switch (resultSet.getString("stockLedgerGroupId")) {
                    case "02010":
                        reportingResult.setTaxRelevantAmount(BigDecimal.valueOf(resultSet.getDouble("SUM(taxValueAmount)")));
                        break;
                    case "02030":
                        reportingResult.setThirdCoundryNotTaxRelevantAmount(BigDecimal.valueOf(resultSet.getDouble("SUM(taxValueAmount)")));
                        break;
                    case "02040":
                        reportingResult.setNotTaxRelevantAmount(BigDecimal.valueOf(resultSet.getDouble("SUM(taxValueAmount)")));
                        break;
                }
                if (resultSet.isLast()) {
                    result.add(reportingResult);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            result = new ArrayList<>(); // Return an empty result in case of an error
        }
        return result;
    }
}
