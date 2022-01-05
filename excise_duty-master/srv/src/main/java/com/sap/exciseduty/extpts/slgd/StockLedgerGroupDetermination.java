package com.sap.exciseduty.extpts.slgd;

import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.exciseduty.entities.repositories.ExtensionFunctionConfigRepository;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.ExtensionFunctionConfig;
import com.sap.exciseduty.extpts.Extension;
import com.sap.exciseduty.extpts.FaasExtensionPoint;
import com.sap.exciseduty.extpts.StockLedgerGroupDeterminationExtensionPoint;
import com.sap.exciseduty.extpts.StockLedgerGroupDeterminationExtensionPoint.Input;
import com.sap.exciseduty.extpts.StockLedgerGroupDeterminationExtensionPoint.Output;
import com.sap.exciseduty.processors.exceptions.StockLedgerGroupMappingNotFound;
import com.sap.exciseduty.processors.exceptions.StockLedgerGroupNotFoundException;

public class StockLedgerGroupDetermination extends FaasExtensionPoint<Input, Output>
        implements StockLedgerGroupDeterminationExtensionPoint {

    private static final String EXTENSION_POINT_NAME = "StockLedgerDet";

    public StockLedgerGroupDetermination(DataSourceHandler dataSourceHandler) {
        super(getDestination(EXTENSION_POINT_NAME, dataSourceHandler), StockLedgerGroupDetermination.Output.class);
    }

    @Override
    public Output call(Input input, Extension<Input, Output> defaultImplementation)
            throws StockLedgerGroupNotFoundException, StockLedgerGroupMappingNotFound {
        try {
            
            // execute defaultImplementation and add result values to input of extension point
            StockLedgerGroupDetermination.Output defaultOutput = defaultImplementation.call(input);
            input.getStockLedgerLineItem().setStockLedgerDivision(defaultOutput.getStockLedgerDivision());
            input.getStockLedgerLineItem().setStockLedgerGroupId(defaultOutput.getStockLedgerGroupId());
            input.getStockLedgerLineItem().setStockLedgerNumber(defaultOutput.getStockLedgerNumber());
            input.getStockLedgerLineItem().setStockLedgerSubdivision(defaultOutput.getStockLedgerSubdivision());
            
            // prepare a default implementation (in case no FaaS implementation is available)
            // in this case, we just return the result of the original default implementation 
            Extension<Input, Output> newDefaultImplementation = i -> {
                return defaultOutput;
            };
                          
            // invoke the faas implementation
            return super.call(input, newDefaultImplementation);
        } catch (StockLedgerGroupNotFoundException | StockLedgerGroupMappingNotFound e) {
            throw e;
        } catch (Exception e) {
            throw new ExecutionException(this, e);
        }
    }

    @Override
    public String getExtensionPointName() {
        return EXTENSION_POINT_NAME;
    }

    /*
     * 
     */
    private static Destination getDestination(String extensionPointName, DataSourceHandler dataSourceHandler)
            throws ExecutionException {
        try {
            ExtensionFunctionConfigRepository repo = new ExtensionFunctionConfigRepository(dataSourceHandler);
            ExtensionFunctionConfig extensionConfig = repo.getByKey("BI", extensionPointName);
            return DestinationAccessor.getDestination(extensionConfig.getExtensionDestination());
        } catch (EntityNotFoundException e) {
            return null;
        } catch (DestinationNotFoundException e) {
            throw new ExecutionException(EXTENSION_POINT_NAME, e);
        }
    }
}
