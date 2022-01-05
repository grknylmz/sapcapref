package com.sap.exciseduty.processors;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationAccessException;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.exciseduty.client.s4worklist.IS4WorklistStatus;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistStatusItem;
import com.sap.exciseduty.entities.IStockLedgerLineItem;
import com.sap.exciseduty.entities.repositories.StockLedgerLineItemRepository;
import com.sap.exciseduty.entities.repositories.StockLedgerProcessingErrorRepository;
import com.sap.exciseduty.entities.repositories.exceptions.EntityDuplicationException;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerLineItem;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerProcessingError;
import com.sap.exciseduty.processors.exceptions.ProcessingException;
import com.sap.exciseduty.processors.workers.AccountingDocumentWorker;
import com.sap.exciseduty.processors.workers.ILineItemWorker;
import com.sap.exciseduty.processors.workers.StockLedgerLineItemWorker;

public class LineItemPostingProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSourceHandler dataSourceHandler;
    private final List<ILineItemWorker> workers;

    public LineItemPostingProcessor(DataSourceHandler dataSourceHandler)
            throws DestinationNotFoundException, DestinationAccessException {
        this.dataSourceHandler = dataSourceHandler;
        this.workers = registerWorkers();
    }

    public S4WorklistStatusItem process(S4WorklistItem worklistItem) {
        return processInternal(worklistItem, false);
    }

    public S4WorklistStatusItem reprocess(S4WorklistItem worklistItem) {
        return processInternal(worklistItem, true);
    }

    private S4WorklistStatusItem processInternal(S4WorklistItem worklistItem, Boolean isReprocessingMode) {
        List<StockLedgerLineItem> stockLedgerLineItems = new ArrayList<>();
        logger.debug("Processing line item " + worklistItem.getMaterialYear() + " "
                + worklistItem.getMaterialDocumentNumber() + " " + worklistItem.getLine());
        for (ILineItemWorker worker : workers) {
            logger.debug("Invoking worker " + worker.getClass().getName() + " (reprocessing = "
                    + isReprocessingMode.toString() + ")");
            try {
                if (isReprocessingMode) {
                    worker.reprocess(worklistItem, stockLedgerLineItems);
                } else {
                    worker.process(worklistItem, stockLedgerLineItems);
                }
            } catch (ProcessingException e) {
                writeErrorLog(worklistItem, e.getMessage());
                return new S4WorklistStatusItem(worklistItem, IS4WorklistStatus.ERROR);
            }
        }

        try {
            persistLineItems(stockLedgerLineItems);
        } catch (EntityDuplicationException e) {
            // e.printStackTrace();
            logger.info(e.getMessage());
            return new S4WorklistStatusItem(worklistItem, IS4WorklistStatus.ERROR);
        }

        return new S4WorklistStatusItem(worklistItem, IS4WorklistStatus.PROCESSED);
    }

    protected void persistLineItems(List<StockLedgerLineItem> lineItems) throws EntityDuplicationException {

        StockLedgerLineItemRepository repo = new StockLedgerLineItemRepository(this.dataSourceHandler);
        for (StockLedgerLineItem lineItem : lineItems) {

            // added forWORKAROUND for GATEWAY 1.21.1 + S4SDK 2.4.2 (can be removed)
            if (repo.exists(lineItem)) {
                throw new EntityDuplicationException(IStockLedgerLineItem.NAME);
            }

            repo.insert(lineItem);
        }
    }

    protected List<ILineItemWorker> registerWorkers() throws DestinationNotFoundException, DestinationAccessException {
        List<ILineItemWorker> workers = new ArrayList<>();
        workers.add(new StockLedgerLineItemWorker(dataSourceHandler));
        workers.add(new AccountingDocumentWorker());
        return workers;
    }

    public void writeErrorLog(S4WorklistItem worklistItem, String erroMessage) {
        StockLedgerProcessingErrorRepository repo = new StockLedgerProcessingErrorRepository(dataSourceHandler);
        StockLedgerProcessingError error = new StockLedgerProcessingError();

        error.setMaterialDocumentNumber(worklistItem.getMaterialDocumentNumber());
        error.setMaterialDocumentYear(worklistItem.getMaterialYear());
        error.setMaterialDocumentItem(worklistItem.getLine());
        // TODO: ???
        error.setExciseDutyPositionNumber(1);
        error.setCompanyCode(worklistItem.getCompanyCode());
        error.setPlant(worklistItem.getPlant());
        error.setStorageLocation(worklistItem.getStorageLocation());
        error.setMaterialDocumentPostingDate(worklistItem.getPostingDateInDocument());
        error.setMaterialNumber(worklistItem.getMaterialNumber());
        error.setErrorText(erroMessage);

        repo.upsert(error);
    }
}
