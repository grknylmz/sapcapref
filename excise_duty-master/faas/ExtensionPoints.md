

# Extension Points
ExtensionPoints are predefined locations in the application code that allow you to change the default behavior.
Each ExtensionPoint is defined by three attributes that describe it completely:
* the **Input** - the data which is passed to the ExtensionPoint (what goes in)
* the **Output** - the data which is returned from the ExtensionPoint (what comes out)
* the **Documentation** - the purpose of the Extension Point (what is it good for)

In our framework, an Extension Point is represented by a class which implements the interface `ExtensionPoint`.
``` java 
public interface ExtensionPoint<Input, Output> {

    public Output call(Input input, Extension<Input, Output> defaultImplementation) throws Exception;

    default Output call(Input input) throws Exception {
        return call(input, null);
    }

    public String getExtensionPointName();

    [...]
}
```

A concrete `ExtensionPoint` will extend this interface, and will basically define the data types for `Input` and `Output`, and the business semantics.
Let's visualize this with an example:
``` java
package com.sap.exciseduty.processors.workers;

import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.entities.repositories.pojos.ShipToMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerLineItem;
import com.sap.exciseduty.entities.repositories.pojos.TaxWarehouse;
import com.sap.exciseduty.extpts.Extension;
import com.sap.exciseduty.extpts.ExtensionPoint;
import com.sap.exciseduty.extpts.slgd.CustomerGroupSpecialPartnerAssignment;
import com.sap.exciseduty.extpts.slgd.ShipToMasterExtensionEU;
import com.sap.exciseduty.processors.exceptions.StockLedgerGroupMappingNotFound;
import com.sap.exciseduty.processors.exceptions.StockLedgerGroupNotFoundException;
import com.sap.exciseduty.processors.workers.StockLedgerGroupDeterminationExtensionPoint.Input;
import com.sap.exciseduty.processors.workers.StockLedgerGroupDeterminationExtensionPoint.Output;

/**
 * 
 * @author d021248
 *         This extensionPoint will determine the StockLedgerGroup.
 *         (detailed description...)
 * 
 *         Input:
 *         - material master data
 *         - receiver information
 *         (detailed description...)
 * 
 *         Output:
 *         the stock ledger group
 *         (detailed description...)
 *
 *         Exceptions:
 *         - StockLedgerGroupNotFoundException
 *         - StockLedgerGroupMappingNotFound
 *
 */
public interface StockLedgerGroupDeterminationExtensionPoint extends ExtensionPoint<Input, Output> {

    final static String EXTENSION_POINT_NAME = "StockLedgerGroupDetermination";

    @Override
    public Output call(Input input, Extension<Input, Output> defaultImplementation) throws StockLedgerGroupNotFoundException, StockLedgerGroupMappingNotFound;


    public static class Input {

        private S4WorklistItem S4WorklistItem;
        private StockLedgerLineItem StockLedgerLineItem;
        private ShipToMasterExtension ShipToMasterExtension;
        private ShipToMasterExtensionEU ShipToMasterExtensionEU;
        private TaxWarehouse TaxWarehouse;
        private CustomerGroupSpecialPartnerAssignment CustomerGroupSpecialPartnerAssignment;

        public S4WorklistItem getS4WorklistItem() {
            return S4WorklistItem;
        }

        public void setS4WorklistItem(S4WorklistItem s4WorklistItem) {
            S4WorklistItem = s4WorklistItem;
        }

        public StockLedgerLineItem getStockLedgerLineItem() {
            return StockLedgerLineItem;
        }

        public void setStockLedgerLineItem(StockLedgerLineItem stockLedgerLineItem) {
            StockLedgerLineItem = stockLedgerLineItem;
        }

        public ShipToMasterExtension getShipToMasterExtension() {
            return ShipToMasterExtension;
        }

        public void setShipToMasterExtension(ShipToMasterExtension shipToMasterExtension) {
            ShipToMasterExtension = shipToMasterExtension;
        }

        public ShipToMasterExtensionEU getShipToMasterExtensionEU() {
            return ShipToMasterExtensionEU;
        }

        public void setShipToMasterExtensionEU(ShipToMasterExtensionEU shipToMasterExtensionEU) {
            ShipToMasterExtensionEU = shipToMasterExtensionEU;
        }

        public TaxWarehouse getTaxWarehouse() {
            return TaxWarehouse;
        }

        public void setTaxWarehouse(TaxWarehouse taxWarehouse) {
            TaxWarehouse = taxWarehouse;
        }

        public CustomerGroupSpecialPartnerAssignment getCustomerGroupSpecialPartnerAssignment() {
            return CustomerGroupSpecialPartnerAssignment;
        }

        public void setCustomerGroupSpecialPartnerAssignment(CustomerGroupSpecialPartnerAssignment customerGroupSpecialPartnerAssignment) {
            CustomerGroupSpecialPartnerAssignment = customerGroupSpecialPartnerAssignment;
        }
    }

    public static class Output {

        private String stockLedgerGroupId;

        public String getStockLedgerGroupId() {
            return stockLedgerGroupId;
        }

        public void setStockLedgerGroupId(String stockLedgerGroupId) {
            this.stockLedgerGroupId = stockLedgerGroupId;
        }
    }
}
```

The class `StockLedgerGroupDeterminationExtensionPoint` acts as the contract for implementers of the Extension Point. It specifies the `API` by defining the data types for the `Input`and `Output`parameters, as well as providing a meaningfull description of the business semantics. The interface should be published to the parties (consultants, partners...), who want to provide an implementation for this Extension Point.


# Implementation
An implementation of an `ExtensionPoint` is a class which implements the `ExtensionPointInterface`.

There are two types of implementations:

* **worker-implementations:**
  * these workers are implementing the actual business logic
  * they process the data provided as `Input`  and return a result of type `Output`

* **dispatcher-implementations:** 
  * these implementations do not execute business logic
  * they are used to configure the Extension Point
  * at runtime, these implementations will determine a concrete worker-implementation, and will delegate the actual processing of the data to it

>Note:
>  * Technically, there is no difference between a worker-implementation and a dispatcher-implementation of an Extension Point.
>  * Both are implementing the interface of the Extension Point.
>  * The difference is the usage scenario (dispatch or do business logic).



Let's continue with our example from above:
* We have already defined an Extension Point `StockLedgerGroupDeterminationExtensionPoint`.
* We will now create a dispatcher-implementation, which will be used to search for a `FaaS Endpoint`, which will then do the actuall work.
* Therefore, this class will look whether an appropriate `FaaS Endpoint` has been configured on the database.
  * if yes, it will determine the corresponding `Destination` and will invoke the `FaaS Endpoint`
  * if not, it will invoke the `defaultImplementation`
* The implementing class is `StockLedgerGroupDetermination`. 

We will skip the implementation details for this class, but we will show how the `ExtensionPoint` coding is embedded in the starndard code.
The Extension Point **StockLedgerGroupDetermination** is located in class `StockLedgerLineItemWorker`:
``` java
[...]

            /*
             * Extension Point StockLedgerGroupDetermination
             */

            // step 1: embed an ExtensionPoint 
            StockLedgerGroupDeterminationExtensionPoint extensionPoint = new StockLedgerGroupDetermination(handler);

            // step 2: prepare input
            StockLedgerGroupDetermination.Input slgdi = new StockLedgerGroupDetermination.Input();
            slgdi.setS4WorklistItem(worklistItem);
            slgdi.setStockLedgerLineItem(stockLedgerLineItem);
            slgdi.setShipToMasterExtension(shipToExtension);
            slgdi.setTaxWarehouse(taxWarehouse);
            slgdi.setShipToMasterExtensionEU(shipToMasterExtensionEU);
            slgdi.setCustomerGroupSpecialPartnerAssignment(customerGroupSpecialPartnerAssignment);

            // step 3: prepare default implementation
            Extension<StockLedgerGroupDetermination.Input, StockLedgerGroupDetermination.Output> defaultImplementation = input -> {
                String stockLedgerGroupId = determineStockLedgerGroup(businessTransactionType, movementCategory);
                StockLedgerGroupDetermination.Output output = new StockLedgerGroupDetermination.Output();
                output.setStockLedgerGroupId(stockLedgerGroupId);
                return output;
            };

            // step 4: call ExtensionPoint
            StockLedgerGroupDetermination.Output output = extensionPoint.call(slgdi, defaultImplementation);

            // step 5: validate and transfer result (StockLedgerGroupId)
            // output values have been set either by the configured extension, or by the defaultImplementation
            if (output == null || output.getStockLedgerGroupId() == null || output.getStockLedgerGroupId().isEmpty()) {
                throw new StockLedgerGroupNotFoundException();
            }
            String stockLedgerGroupId = output.getStockLedgerGroupId();

            // the following steps are standard coding...

[...]

```
# Configuration
Extension Points must be configurable. 
Depending on certain characteristics (which tenant, which product, which tax-reason, ...) it may be necessary to pick one or another implementation for an Extension Point. Also, a default behavior should be defined, in case no specific implementation can be picked. This can be achieved with a dispatcher-implementation. 

* It is the task of the dispatcher-implementations to know about the Configuration details of an Extension Point.
* A dispatcher-implementation allows to individually configure an Extension Point.
* In our example, we provide a small UI which allows to configure the Extension Points. 
* The Configuration is then persisted on the database. 
* At runtime, our dispatcher-implementation (`StockLedgerGroupDetermination`) will access the database.
* If the Extension Point `StockLedgerGroupDetermination` has been configured, the Configuration is read from the database and is used to determine a `Destination` for a `FaaS Endpoint`.
* If no Configuration is available for the Extension Point, the `defaultImplementation` is ivoked instead.

![Configuration](https://github.wdf.sap.corp/ICDCloudArchitcture/excise_duty/blob/feature/extensionSpot/faas/configuration.png)



