package com.sap.exciseduty.odata.extension;

import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.CleanupTransaction;
import com.sap.cloud.sdk.service.prov.api.request.GenericRequest;
import com.sap.cloud.sdk.service.prov.api.request.OperationRequest;
import com.sap.cloud.sdk.service.prov.api.request.Request;
import com.sap.cloud.sdk.service.prov.api.request.impl.OperationRequestImpl;
import com.sap.exciseduty.client.ClientFactory;
import com.sap.exciseduty.client.exception.EventProcessingException;
import com.sap.exciseduty.client.rabbitmq.WorklistEventOrchestrator;
import com.sap.exciseduty.client.rabbitmq.exception.NoRabbitMQConnectionException;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistStatusItem;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.odata.actions.ThreadLocalS4WorklistStatusItem;

public class CleanupTransactionExtension {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @CleanupTransaction(serviceNames = { IExciseDutyEntities.STOCK_LEDGER_SERVICE })
    public void cleanup(boolean isCommitted, List<Request> requests, ExtensionHelper eh) {
        if (isCommitted && requests != null) {
            for (GenericRequest genRequest : requests) {
                if ((genRequest.getClass() == OperationRequestImpl.class) && ((OperationRequest) genRequest)
                        .getOperationName().equals(IExciseDutyEntities.PROCESS_SINGLE_WORKLIST_ITEM_ACTION)) {

                    logger.debug("Called for ProcessSingleWorklistItem");
                    // In this case, we are in the after commit of a single worklist item processing.
                    // This means the item was successfully persisted, and we may report back to the
                    // queue that the item was processed.

                    S4WorklistStatusItem statusItem = ThreadLocalS4WorklistStatusItem.get();
                    if (statusItem != null) {
                        logger.debug("Worklist status item for " + statusItem.getMaterialDocumentYear() + " "
                                + statusItem.getMaterialDocumentNumber() + " " + statusItem.getMaterialDocumentItem());
                        try {
                            WorklistEventOrchestrator eventOrchestrator = ClientFactory.getWorklistEventOrchestrator();
                            eventOrchestrator.sendEvent(statusItem);
                            logger.debug("Event sent to orchestrator");
                        } catch (NoRabbitMQConnectionException | EventProcessingException e) {
                            throw new InternalServerErrorException(e);
                        }

                        // TODO: check this! exit after item has been processed ok?
                        ThreadLocalS4WorklistStatusItem.remove();
                        break;
                    }
                }
            }
        }
    }
}
