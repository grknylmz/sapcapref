package com.sap.exciseduty.odata.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationAccessException;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplex;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.Action;
import com.sap.cloud.sdk.service.prov.api.request.OperationRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.OperationResponse;
import com.sap.exciseduty.client.ClientFactory;
import com.sap.exciseduty.client.exception.S4WorklistStatusUpdateFailedException;
import com.sap.exciseduty.client.s4worklist.IS4WorklistStatus;
import com.sap.exciseduty.client.s4worklist.S4WorklistStatusUpdateClient;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistStatusItem;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.repositories.StockLedgerProcessingErrorRepository;
import com.sap.exciseduty.entities.repositories.exceptions.EntityDeletionFailedException;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerProcessingError;
import com.sap.exciseduty.processors.LineItemPostingProcessor;
import com.sap.exciseduty.utility.VdmMapper;

public class ReprocessProcessingErrorItemAction {

    @Action(Name = "ReprocessItem", serviceName = IExciseDutyEntities.STOCK_LEDGER_PROCESSING_ERROR_SERVICE)
    public OperationResponse execute(OperationRequest functionRequest, ExtensionHelper extensionHelper) {
        LineItemPostingProcessor processor;
        try {
            processor = new LineItemPostingProcessor(extensionHelper.getHandler());
        } catch (DestinationNotFoundException | DestinationAccessException e1) {
            return OperationResponse.setError(ErrorResponse.getBuilder().setMessage("CloudConnector Setup Error: " + e1.getMessage()).setStatusCode(502).response());
        }
        StockLedgerProcessingErrorRepository repo = new StockLedgerProcessingErrorRepository(extensionHelper.getHandler());
        Map<String, Object> parameters = functionRequest.getParameters();

        // Read error entry
        StockLedgerProcessingError processingError;
        try {
            processingError = repo.getByKey(
                    (String) parameters.get("materialDocumentItem"),
                    (String) parameters.get("materialDocumentNumber"),
                    (String) parameters.get("materialDocumentYear"),
                    new Integer((String) parameters.get("exciseDutyPositionNumber")));
        } catch (EntityNotFoundException e) {
            return OperationResponse.setError(ErrorResponse.getBuilder().setMessage("Stock Ledger Line Item Error not found").setStatusCode(404).response());
        }

        // Fetch Worklist item to be reprocessed
        ExciseDutyComplex exciseDutyComplex = (ExciseDutyComplex) ClientFactory.getWorklistItemByKeyCommand(new ErpConfigContext("ExciseDuty-S4-onPremise"), (String) parameters.get("materialDocumentNumber"),
                (String) parameters.get("materialDocumentYear"), (String) parameters.get("materialDocumentItem"), (String) parameters.get("billOfMaterialItemNodeNumber")).execute();

        if (exciseDutyComplex == null) {
            return OperationResponse.setError(ErrorResponse.getBuilder().setMessage("Worklist Item for Stock Ledger Line Item Error not found").setStatusCode(404).response());
        }

        S4WorklistItem worklistItem = VdmMapper.mapWorklistItem(exciseDutyComplex);

        // Process worklist item
        try {
            S4WorklistStatusItem worklistStatusItem = processor.reprocess(worklistItem);
            if (worklistStatusItem.getWorklistItemProcessingStatus() == IS4WorklistStatus.PROCESSED) {
                repo.delete(processingError); // Delete error entry for reprocessing

                // Set WorklistItem To processed
                S4WorklistStatusUpdateClient client = new S4WorklistStatusUpdateClient();
                client.updateWorklistItemStatus(worklistItem, IS4WorklistStatus.PROCESSED);
            }
        } catch (EntityDeletionFailedException e) {
            return OperationResponse.setError(ErrorResponse.getBuilder().setMessage("Stock Ledeger Line Item Error deletion failed").response());
        } catch (DestinationNotFoundException | DestinationAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (S4WorklistStatusUpdateFailedException e) {
            return OperationResponse.setError(ErrorResponse.getBuilder().setMessage("Failure during worklist item status update: " + e.getMessage()).response());
        }

        // Build Response
        List<Object> result = new ArrayList<>();
        result.add(String.format(
                "LineItem with MaterialDocumentNumber: %1s, MaterialDocumentYear: %2s and MaterialDocumentYear %3s processed",
                parameters.get("materialDocumentItem"), parameters.get("materialDocumentNumber"),
                parameters.get("materialDocumentYear")));
        return OperationResponse.setSuccess().setPrimitiveData(result).response();
    }
}
