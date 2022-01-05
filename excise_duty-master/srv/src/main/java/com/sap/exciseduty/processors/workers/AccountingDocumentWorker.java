package com.sap.exciseduty.processors.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationAccessException;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;
import com.sap.exciseduty.client.exception.S4AccountingDocumentPostingException;
import com.sap.exciseduty.client.s4finposting.S4AccountingDocumentPostingClient;
import com.sap.exciseduty.client.s4finposting.pojo.AccountingDocumentProjection;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerLineItem;
import com.sap.exciseduty.processors.exceptions.LineItemIsNotAccountingRelevantException;
import com.sap.exciseduty.processors.exceptions.ProcessingException;

public class AccountingDocumentWorker implements ILineItemWorker {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private S4AccountingDocumentPostingClient accDocPostingClient;

    public AccountingDocumentWorker() throws DestinationNotFoundException, DestinationAccessException {
        this.accDocPostingClient = new S4AccountingDocumentPostingClient();
    }

    @Override
    public void process(S4WorklistItem worklistItem, List<StockLedgerLineItem> stockLedgerLineItems) throws ProcessingException {

        for (StockLedgerLineItem lineItem : stockLedgerLineItems) {
            try {
                postAccountingDocument(lineItem);

            } catch (LineItemIsNotAccountingRelevantException e) {
                // Continue processing regularly.

            } catch (S4AccountingDocumentPostingException e) {
                // Abort processing for entire block.
                throw new ProcessingException(e.getMessage());
            }
        }
    }

    @Override
    public void reprocess(S4WorklistItem worklistItem, List<StockLedgerLineItem> stockLedgerLineItems) throws ProcessingException {
        // first cancel any documents already created in FIN
        logger.warn("Reprocessing of AccountDocument Posting triggered although no implementation for cancelling any previous posting is implemented yet");

        // then resume normal processing
        this.process(worklistItem, stockLedgerLineItems);
    }

    protected void postAccountingDocument(StockLedgerLineItem lineItem)
            throws LineItemIsNotAccountingRelevantException, S4AccountingDocumentPostingException {
        // ToDo: Do some actual meaningful error handling (not just passing
        // String messages...)
        switch (lineItem.getStockLedgerGroupId()) {
            case "02010":

                AccountingDocumentProjection accDoc = new AccountingDocumentProjection();

                accDoc.setCompanyCode(lineItem.getCompanyCode());
                accDoc.setPlant(lineItem.getPlant());
                accDoc.setGlAccount1("0000175500");
                accDoc.setGlAccount2("0000468000");
                accDoc.setPostingDate(lineItem.getMaterialDocumentPostingDate());
                accDoc.setQuantity(new BigDecimal(1));
                accDoc.setBaseUnitOfMeasure("EA");
                accDoc.setTaxAmount(lineItem.getTaxValueAmount());
                accDoc.setTaxCurrency(lineItem.getTaxValueCurrency());
                accDoc.setReferenceMaterialDocumentNumber(lineItem.getMaterialDocumentNumber());
                accDoc.setReferenceMaterialDocumentYear(lineItem.getMaterialDocumentYear());
                accDoc.setReferenceObjectSystem("ER8CLNT070"); // TODO: Store in Config/read from inbound interface
                accDoc.setBusinessAction("RMWL");
                accDoc.setUsername("ZIMMERMANNDA"); // TODO: Take User from original material document once it's in the
                                                    // inbound OData service

                String result = accDocPostingClient.postAccountingDocument(accDoc);

                lineItem.setAccountingJournalReference(result);
                break;
            case "02060":
                // ToDo: Would also be Acc Doc relevant but not yet specified
                throw new LineItemIsNotAccountingRelevantException();
            default:
                // No tax posting required.
                throw new LineItemIsNotAccountingRelevantException();
        }

    }

}
