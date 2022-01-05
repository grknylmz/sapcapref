package com.sap.exciseduty.odata.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationAccessException;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.Action;
import com.sap.cloud.sdk.service.prov.api.request.OperationRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.OperationResponse;
import com.sap.exciseduty.client.s4worklist.IS4WorklistStatus;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistStatusItem;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.processors.LineItemPostingProcessor;

public class ProcessSingleWorklistItemAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Action(Name = "ProcessSingleWorklistItem", serviceName = IExciseDutyEntities.STOCK_LEDGER_SERVICE)
    public OperationResponse executeSingle(OperationRequest functionRequest, ExtensionHelper extensionHelper) {

        Map<String, Object> params = functionRequest.getParameters();

        String materialDocumentYear = params.get("materialDocumentYear").toString();
        String materialDocumentNumber = params.get("materialDocumentNumber").toString();
        String materialDocumentItem = params.get("materialDocumentItem").toString();

        logger.info("Processing: " + materialDocumentYear + " " + materialDocumentNumber + " " + materialDocumentItem);
        S4WorklistItem worklistItem = S4WorklistItemBuffer.get(materialDocumentYear, materialDocumentNumber, materialDocumentItem);

        try {
            S4WorklistStatusItem statusItem = processLineItem(functionRequest, extensionHelper.getHandler(), worklistItem);

            ThreadLocalS4WorklistStatusItem.set(statusItem); // set instance for anonymous processing

            List<Object> result = new ArrayList<>();
            result.add(materialDocumentYear + " " + materialDocumentNumber + " " + materialDocumentItem);
            logger.info("Successful processed: " + materialDocumentYear + " " + materialDocumentNumber + " " + materialDocumentItem);
            return OperationResponse.setSuccess().setPrimitiveData(result).response();

        } catch (DestinationNotFoundException | DestinationAccessException e) {
            logger.info("Failed processing: " + materialDocumentYear + " " + materialDocumentNumber + " " + materialDocumentItem);
            e.printStackTrace();
            return OperationResponse.setError(ErrorResponse.getBuilder().setMessage(e.getMessage()).response());
        }
    }

    protected S4WorklistStatusItem processLineItem(OperationRequest functionRequest, DataSourceHandler dataSourceHandler, S4WorklistItem worklistItem) throws DestinationNotFoundException, DestinationAccessException {
        LineItemPostingProcessor processor = new LineItemPostingProcessor(dataSourceHandler);

        switch (worklistItem.getProcessingStatus()) {
            case IS4WorklistStatus.PENDING:
                // First time the item is processed. Work in the optimized mode.
                return processor.process(worklistItem);
            case IS4WorklistStatus.QUEUED:
            case IS4WorklistStatus.ERROR:
                // Item has been processed before, therefore execute in reprocessing mode.
                return processor.reprocess(worklistItem);
            default:
                return null;
        }
    }
}
