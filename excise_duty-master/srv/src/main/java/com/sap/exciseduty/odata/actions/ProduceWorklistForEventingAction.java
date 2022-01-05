package com.sap.exciseduty.odata.actions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.InternalServerErrorException;

import org.apache.http.HttpStatus;
import org.apache.olingo.odata2.api.commons.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.GetResponse;
import com.sap.cloud.sdk.cloudplatform.CloudPlatformAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationAccessException;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;
import com.sap.cloud.sdk.cloudplatform.servlet.RequestContextExecutor;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplex;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.Action;
import com.sap.cloud.sdk.service.prov.api.request.OperationRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.OperationResponse;
import com.sap.exciseduty.client.ClientFactory;
import com.sap.exciseduty.client.exception.EventProcessingException;
import com.sap.exciseduty.client.exception.S4WorklistStatusUpdateFailedException;
import com.sap.exciseduty.client.rabbitmq.WorklistEventOrchestrator;
import com.sap.exciseduty.client.rabbitmq.exception.NoRabbitMQConnectionException;
import com.sap.exciseduty.client.s4worklist.IS4WorklistStatus;
import com.sap.exciseduty.client.s4worklist.S4WorklistStatusUpdateClient;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistStatusItem;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.utility.EnvironmentHelper;
import com.sap.exciseduty.utility.VdmMapper;
import com.sap.exciseduty.utility.WebSecurityConfig;
import com.sap.exciseduty.workaround.CustomScpCfCloudPlatformFacade;
import com.sap.xs2.security.container.SecurityContext;
import com.sap.xs2.security.container.UserInfoException;

public class ProduceWorklistForEventingAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int PAGE_SIZE = Integer.parseInt(EnvironmentHelper.getVariableWithDefault("S4_WORKLIST_PAGE_SIZE", "10"));
    private static ExecutorService executor = Executors.newCachedThreadPool();

    @Action(Name = "ProduceWorklistForEventing", serviceName = IExciseDutyEntities.STOCK_LEDGER_SERVICE)
    public OperationResponse execute(OperationRequest functionRequest, ExtensionHelper extensionHelper) {
        logger.debug("ProduceWorklistForEventing action called");

        // Verify Scope of incoming Request
        try {
            if (!SecurityContext.getUserInfo().checkLocalScope(WebSecurityConfig.SLEDIT_SCOPE)) {
                return OperationResponse.setError((ErrorResponse.getBuilder().setMessage("Forbidden").setStatusCode(HttpStatus.SC_FORBIDDEN)).response());
            }
        } catch (UserInfoException e1) {
            return OperationResponse.setError((ErrorResponse.getBuilder().setMessage("Internal Server Error").setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)).response());
        }

        if (!isRequestValid(functionRequest)) {
            logger.error("Input Validation vailed");
            functionRequest.getMessageContainer().addErrorMessage("validations.inputValidation.MissingHeader", "StockLedgerLineItem", HttpHeaders.AUTHORIZATION.toString());
            return OperationResponse
                    .setError(ErrorResponse.getBuilder().setMessage("validations.inputValidation")
                            .addContainerMessages()
                            .setStatusCode(com.sap.gateway.core.api.enums.HttpStatus.BAD_REQUEST.getStatusCode())
                            .response());
        }
        try {
//            Runnable runnableTask = () -> {
//                this.executeInternal(functionRequest, extensionHelper.getHandler());
//            };
//            executor.submit(runnableTask);        	        	
            
              this.executeInternal(functionRequest, extensionHelper.getHandler());
            
//            RequestContextExecutor rce = new RequestContextExecutor();
//            rce.execute(()-> this.executeInternal(functionRequest, extensionHelper.getHandler()));
            
        } catch (Exception e) {
            return OperationResponse.setError((ErrorResponse.getBuilder().setMessage("Failure during worklist item processing: " + e.getMessage())).response());
        }

        List<Object> result = new ArrayList<>();
        result.add("Processing started");

        return OperationResponse.setSuccess().setPrimitiveData(result).response();
    }

    protected void executeInternal(OperationRequest functionRequest, DataSourceHandler dataSourceHandler) {

        // Get all relevant worklistItems to be processed
        int workListItemsRetrievedOverall = 0;
        int worklistItemsRetrieved = 0;

        try {
            WorklistEventOrchestrator orchestrator = ClientFactory.getWorklistEventOrchestrator();

            do {
                List<ExciseDutyComplex> exciseDutyList = retrieveExciseDutyList(PAGE_SIZE);
                worklistItemsRetrieved = exciseDutyList.size();
                workListItemsRetrievedOverall += exciseDutyList.size();
                if (exciseDutyList.size() == 0) {
                    continue;
                }

                // Set all work list items to queued to prevent further processing by another job
                // TODO: Verify that status update was successful
                if (exciseDutyList.size() > 0) {
                    logger.debug("Setting status for selected items to queued.");
                    // map from VDM to Worklist item
                    List<S4WorklistItem> worklist = VdmMapper.mapCollection(exciseDutyList, VdmMapper::mapWorklistItem);
                    // Set Status in OnPremise
                    setStatus(worklist, IS4WorklistStatus.QUEUED);
                    // Submit work list items to event queue
                    orchestrator.sendEvent(worklist);
                    // TODO: Verify if all items could be submitted otherwise roll back status in S/4 to pending
                }
            } while (worklistItemsRetrieved == PAGE_SIZE);

            // Send Status Update to S4 for processed events
            GetResponse statusEvent = orchestrator.readStatusEventQueue();
            logger.debug("Checking for status updates from queue to send back to ERP.");

            List<S4WorklistItem> worklistItemStatusError = new ArrayList<>();
            List<S4WorklistItem> worklistItemStatusProcessed = new ArrayList<>();
            Gson gson = new Gson();
            if (statusEvent != null) {
                int count = statusEvent.getMessageCount();
                logger.debug(count + " messages pending.");
                for (int i = 0; i <= count; i++) {
                    if (statusEvent == null) break;
                    String message = new String(statusEvent.getBody(), "UTF-8");
                    S4WorklistStatusItem statusItem = gson.fromJson(message, S4WorklistStatusItem.class);

                    S4WorklistItem worklistItem = getS4WorklistItem(statusItem);
                    if (statusItem.getWorklistItemProcessingStatus().equals(IS4WorklistStatus.ERROR)) {
                        worklistItemStatusError.add(worklistItem);
                    } else if (statusItem.getWorklistItemProcessingStatus().equals(IS4WorklistStatus.PROCESSED)) {
                        worklistItemStatusProcessed.add(worklistItem);
                    }
                    statusEvent = orchestrator.readStatusEventQueue();
                }
            }

            if (worklistItemStatusError.size() > 0) {
                setStatus(worklistItemStatusError, IS4WorklistStatus.ERROR);
            }
            if (worklistItemStatusProcessed.size() > 0) {
                setStatus(worklistItemStatusProcessed, IS4WorklistStatus.PROCESSED);
            }
        } catch (UnsupportedEncodingException | S4WorklistStatusUpdateFailedException | EventProcessingException | NoRabbitMQConnectionException e) {
            logger.info("An error occurred while processing the worklist items: {} ", e);
        }
        logger.info("Worklist Items processed: " + workListItemsRetrievedOverall);
    }

    private S4WorklistItem getS4WorklistItem(S4WorklistStatusItem statusItem) {
        S4WorklistItem worklistItem = new S4WorklistItem();
        worklistItem.setMaterialYear(statusItem.getMaterialDocumentYear());
        worklistItem.setMaterialDocumentNumber(statusItem.getMaterialDocumentNumber());
        worklistItem.setLine(statusItem.getMaterialDocumentItem());
        worklistItem.setBillOfMaterialItemNodeNumber(statusItem.getBillOfMaterialItemNodeNumber());
        return worklistItem;
    }

    @SuppressWarnings("unchecked")
    protected List<ExciseDutyComplex> retrieveExciseDutyList(int topCount) {
        return (List<ExciseDutyComplex>) ClientFactory.getWorklistItemCommand(new ErpConfigContext("ExciseDuty-S4-onPremise"), IS4WorklistStatus.PENDING, topCount).execute();
    }

    protected void setStatus(List<S4WorklistItem> worklistItems, String status) throws S4WorklistStatusUpdateFailedException {
        try {
            S4WorklistStatusUpdateClient client = new S4WorklistStatusUpdateClient();
            client.updateWorklistItemStatus(worklistItems, status);
        } catch (DestinationNotFoundException | DestinationAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }
    }

    protected boolean isRequestValid(OperationRequest request) {
        return request.containsHeader(HttpHeaders.AUTHORIZATION);
    }
}
