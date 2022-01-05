package com.sap.exciseduty.processors.workers;

import java.util.List;

import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerLineItem;
import com.sap.exciseduty.processors.exceptions.ProcessingException;

public interface ILineItemWorker {
    /**
     * Perform the Line Item Worker's processing based on the original S4 item and any potential existing processing
     * results.
     * In the process action, the processor may assume that this is the first time he receives the item and hence does
     * not have to deal with any checks or cleanup activities.
     * 
     * @param s4WorklistItem
     * @param stockLedgerLineItems
     * @return
     * @throws ProcessingException
     */
    public void process(S4WorklistItem worklistItem, List<StockLedgerLineItem> stockLedgerLineItems) throws ProcessingException;

    /**
     * Perform the Line Item Worker's processing but assume .
     * 
     * @param s4WorklistItem
     * @param stockLedgerLineItems
     * @return
     * @throws ProcessingException
     */
    public void reprocess(S4WorklistItem worklistItem, List<StockLedgerLineItem> stockLedgerLineItems) throws ProcessingException;

}
