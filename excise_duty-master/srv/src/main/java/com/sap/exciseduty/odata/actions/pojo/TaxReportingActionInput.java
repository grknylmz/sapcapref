package com.sap.exciseduty.odata.actions.pojo;

import java.time.LocalDate;
import java.util.Map;

import com.sap.exciseduty.utility.EntityHelper;

public class TaxReportingActionInput {
    String taxWarehouseRegistration;
    LocalDate fromDate;
    LocalDate toDate;

    public TaxReportingActionInput(Map<String, Object> parameters) {
        fromDate = EntityHelper.convertDate((String) parameters.get("fromDate"));
        toDate = EntityHelper.convertDate((String) parameters.get("toDate"));
        taxWarehouseRegistration = (String) parameters.get("taxWarehouseRegistration");
    }

    public String getTaxWarehouseRegistration() {
        return taxWarehouseRegistration;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }
}