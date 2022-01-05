package com.sap.exciseduty.processors.workers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.sap.cloud.sdk.hana.connectivity.cds.CDSException;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSQuery;
import com.sap.cloud.sdk.hana.connectivity.cds.CDSSelectQueryBuilder;
import com.sap.cloud.sdk.hana.connectivity.cds.ConditionBuilder;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.rt.cds.CDSHandler;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.repositories.MaterialMasterExtensionRepository;
import com.sap.exciseduty.entities.repositories.MovementCategoryRepository;
import com.sap.exciseduty.entities.repositories.SettlementUnitRepository;
import com.sap.exciseduty.entities.repositories.ShipToMasterExtensionRepository;
import com.sap.exciseduty.entities.repositories.StockLedgerGroupRepository;
import com.sap.exciseduty.entities.repositories.StockLedgerLineItemRepository;
import com.sap.exciseduty.entities.repositories.TaxWarehouseRepository;
import com.sap.exciseduty.entities.repositories.exceptions.EntityDeletionFailedException;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.MaterialMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.MovementCategory;
import com.sap.exciseduty.entities.repositories.pojos.MovementTypeMapping;
import com.sap.exciseduty.entities.repositories.pojos.SettlementUnit;
import com.sap.exciseduty.entities.repositories.pojos.ShipToMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerGroup;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerLineItem;
import com.sap.exciseduty.entities.repositories.pojos.TaxWarehouse;
import com.sap.exciseduty.entities.repositories.pojos.TaxWarehouseAssignment;
import com.sap.exciseduty.extpts.Extension;
import com.sap.exciseduty.extpts.ExtensionPoint;
import com.sap.exciseduty.extpts.StockLedgerGroupDeterminationExtensionPoint;
import com.sap.exciseduty.extpts.slgd.Country;
import com.sap.exciseduty.extpts.slgd.CustomerGroupSpecialPartnerAssignment;
import com.sap.exciseduty.extpts.slgd.ShipToMasterExtensionEU;
import com.sap.exciseduty.extpts.slgd.SpecialPartnerType;
import com.sap.exciseduty.extpts.slgd.StockLedgerGroupDetermination;
import com.sap.exciseduty.processors.TaxCalculationProcessor;
import com.sap.exciseduty.processors.exceptions.ConvertionToSettlementUnitNotPossibleException;
import com.sap.exciseduty.processors.exceptions.MaterialIsNotExciseDutyRelevantException;
import com.sap.exciseduty.processors.exceptions.MovementTypeUnknownException;
import com.sap.exciseduty.processors.exceptions.ProcessingException;
import com.sap.exciseduty.processors.exceptions.StockLedgerGroupMappingNotFound;
import com.sap.exciseduty.processors.exceptions.StockLedgerGroupNotFoundException;
import com.sap.exciseduty.processors.exceptions.TaxRateNotFoundException;
import com.sap.exciseduty.processors.pojos.BusinessTransactionType;
import com.sap.exciseduty.processors.pojos.TaxCalculationInput;

public class StockLedgerLineItemWorker implements ILineItemWorker {

    private DataSourceHandler handler;

    public StockLedgerLineItemWorker(DataSourceHandler dataSourceHandler) {
        this.handler = dataSourceHandler;
    }

    @Override
    public void process(S4WorklistItem worklistItem, List<StockLedgerLineItem> stockLedgerLineItems) throws ProcessingException {
        /*
         * Read Material Master Extension
         */
        List<MaterialMasterExtension> materialExtensions = getMaterialMasterExtension(worklistItem.getMaterialNumber(), worklistItem.getCompanyCode());
        if (materialExtensions == null || materialExtensions.size() == 0) {
            // Material is not maintained in Excise Duty extension
            throw new ProcessingException("Material is not maintained in Material Master Extension");
        }

        for (MaterialMasterExtension materialExtension : materialExtensions) {
            try {
                StockLedgerLineItem lineItem = processLineItemEntry(materialExtension, worklistItem);
                stockLedgerLineItems.add(lineItem);
            } catch (MaterialIsNotExciseDutyRelevantException e) {
                break;
            }
        }
    }

    @Override
    public void reprocess(S4WorklistItem worklistItem, List<StockLedgerLineItem> stockLedgerLineItems) throws ProcessingException {

        StockLedgerLineItemRepository repo = new StockLedgerLineItemRepository(this.handler);
        // first delete any existing items from Repo... Could also be seen as action of the outer method as that one
        // also persists...
        // TODO: query Repo and delete items

        try {
            repo.deleteAllForMaterialDocumentLine(
                    worklistItem.getMaterialDocumentNumber(),
                    worklistItem.getMaterialYear(),
                    worklistItem.getLine());
        } catch (EntityDeletionFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // ...then start normal processing
        this.process(worklistItem, stockLedgerLineItems);
    }

    protected StockLedgerLineItem processLineItemEntry(MaterialMasterExtension materialMasterExtension, S4WorklistItem worklistItem) throws MaterialIsNotExciseDutyRelevantException, ProcessingException {
        TaxCalculationProcessor taxCalculation = new TaxCalculationProcessor(this.handler);
        try {
            /*
             * Read relevant Tax Warehouse
             */
            TaxWarehouse taxWarehouse;
            try {
                taxWarehouse = getTaxWarehouseRegistration(worklistItem.getPlant(), worklistItem.getStorageLocation(), materialMasterExtension.getExciseDutyTypeId());
            } catch (EntityNotFoundException e) {
                // Material Movement is not relevant for excise Duty
                throw new MaterialIsNotExciseDutyRelevantException();
            }

            /*
             * Read ship to master extension
             */
            ShipToMasterExtension shipToExtension;
            try {
                shipToExtension = getShipToMasterExtension(worklistItem.getCustomerNumber(), worklistItem.getPostingDateInDocument());
            } catch (EntityNotFoundException e) {
                // TODO: Evaluate how to proceed if we do not have this
                // information
                throw new MaterialIsNotExciseDutyRelevantException();
            }

            /*
             * Determine BusinessTransactionType (gv_herkunft)
             */
            BusinessTransactionType businessTransactionType = analyseShipToParty(shipToExtension);

            /*
             * Read Movement category
             */
            MovementCategory movementCategory = getMovementCategory(worklistItem.getMovementType(), worklistItem.getMovementIndicator());

            /*
             * Read Settlement Unit
             */
            SettlementUnit settlementUnit = getSettlementUnit(worklistItem.getCompanyCode(), materialMasterExtension.getExciseDutyTypeId());

            StockLedgerLineItem stockLedgerLineItem = initLineItem(worklistItem, materialMasterExtension, taxWarehouse, shipToExtension, businessTransactionType, movementCategory, settlementUnit);

            /*
             * Read ship to master extension for EU
             */
            ShipToMasterExtensionEU shipToMasterExtensionEU = getShipToMasterExtensionEU(worklistItem.getCustomerNumber());

            /*
             * Read Customer Group Special Partner Assignment
             */
            CustomerGroupSpecialPartnerAssignment customerGroupSpecialPartnerAssignment = getCustomerGroupSpecialPartnerAssignment(worklistItem.getZz_kdgrp());



            /*
             * ExtensionPoint StockLedgerGroupDetermination
             */

            // step 1: embed the ExtensionPoint
            StockLedgerGroupDeterminationExtensionPoint extensionPoint = new StockLedgerGroupDetermination(handler);

            // step 2: prepare input
            StockLedgerGroupDetermination.Input slgdi = new StockLedgerGroupDetermination.Input();
            slgdi.setS4WorklistItem(worklistItem);
            slgdi.setStockLedgerLineItem(stockLedgerLineItem);
            slgdi.setShipToMasterExtension(shipToExtension);
            slgdi.setTaxWarehouse(taxWarehouse);
            slgdi.setShipToMasterExtensionEU(shipToMasterExtensionEU);
            slgdi.setCustomerGroupSpecialPartnerAssignment(customerGroupSpecialPartnerAssignment);
            slgdi.setMaterialMasterExtension(materialMasterExtension);

            // step 3: prepare the default (standard) implementation
            Extension<StockLedgerGroupDetermination.Input, StockLedgerGroupDetermination.Output> defaultImplementation = input -> {
                
                
                StockLedgerGroupDetermination.Output output = new StockLedgerGroupDetermination.Output();
                
                String stockLedgerGroupId = getStockLedgerGroupId(businessTransactionType, movementCategory);
                if (stockLedgerGroupId == null || stockLedgerGroupId.isEmpty() ) {
                    throw new StockLedgerGroupNotFoundException();
                }
                
                StockLedgerGroup stockLedgerGroup = getStockLedgerGroup(stockLedgerGroupId);
                if (stockLedgerGroup == null || stockLedgerGroup.getId().isEmpty()) {
                    throw new StockLedgerGroupNotFoundException();
                }
                                
                output.setStockLedgerGroupId(stockLedgerGroupId);
                output.setStockLedgerNumber(stockLedgerGroup.getMovementEntryBehavior());
                output.setStockLedgerGroupId(stockLedgerGroup.getId());
                output.setStockLedgerDivision(stockLedgerGroup.getStockLedgerDivision());
                output.setStockLedgerSubdivision(stockLedgerGroup.getStockLedgerSubdivision());
                                                
                return output;
            };

            // step 4: call ExtensionPoint
            StockLedgerGroupDetermination.Output output = extensionPoint.call(slgdi, defaultImplementation);

            // step 5: transfer result 
            stockLedgerLineItem.setStockLedgerNumber(output.getStockLedgerNumber());
            stockLedgerLineItem.setStockLedgerGroupId(output.getStockLedgerGroupId());
            stockLedgerLineItem.setStockLedgerDivision(output.getStockLedgerDivision());
            stockLedgerLineItem.setStockLedgerSubdivision(output.getStockLedgerSubdivision());


            /*
             * Calculate Taxes
             */
            TaxCalculationInput taxCalculationInput = initTaxCalculationInput(worklistItem, materialMasterExtension, settlementUnit, businessTransactionType);
            taxCalculation.execute(taxCalculationInput, stockLedgerLineItem);

            return stockLedgerLineItem;
        } catch (StockLedgerGroupNotFoundException e) {
            throw new ProcessingException("Stock ledger group does not exist");
            // Write Error (5) into Error Log Entity
        } catch (StockLedgerGroupMappingNotFound e) {
            // TODO Write Error into Error Log Entity
            e.printStackTrace();
        } catch (MovementTypeUnknownException e) {
            // Write Error (9907) into Error Log Entity
            throw new ProcessingException("Movement Type is not assigned to a excise duty type");
        } catch (TaxRateNotFoundException e) {
            // Write Error (9928) into Error Log Entity
            throw new ProcessingException("Tax rate not found");
        } catch (ConvertionToSettlementUnitNotPossibleException e) {
            // TODO Write Error (????) into Error Log Entity
            e.printStackTrace();
        } catch (ExtensionPoint.ExecutionException e) {

            throw new ProcessingException("Call to extension for " + e.getExtensionPointName() + " failed: " + e.getMessage());
        }
        return null;
    }

    protected StockLedgerLineItem initLineItem(S4WorklistItem worklistItem,
            MaterialMasterExtension materialMasterExtension, TaxWarehouse taxWarehouse,
            ShipToMasterExtension shipToExtension, BusinessTransactionType businessTransactionType,
            MovementCategory movementCategory, SettlementUnit settlementUnit) {
        StockLedgerLineItem result = new StockLedgerLineItem();
        result.setMaterialDocumentNumber(worklistItem.getMaterialDocumentNumber());
        result.setMaterialDocumentYear(worklistItem.getMaterialYear());
        result.setMaterialDocumentItem(worklistItem.getLine());
        result.setCompanyCode(worklistItem.getCompanyCode());
        result.setPlant(worklistItem.getPlant());
        result.setStorageLocation(worklistItem.getStorageLocation());
        result.setMaterialDocumentPostingDate(worklistItem.getPostingDateInDocument());
        result.setMaterialNumber(worklistItem.getMaterialNumber());
        result.setBatchNumber(worklistItem.getBatchNumber());
        result.setQuantity(worklistItem.getQuantity());
        result.setBaseUnitOfMeasure(worklistItem.getBaseUnitOfMeasure());
        result.setErpMovementType(worklistItem.getMovementType());
        result.setSalesOrderNumber(worklistItem.getSalesOrderNumber());
        result.setSalesOrderItem(worklistItem.getSalesOrderItemNumber());

        result.setExciseDutyPositionNumber(1);
        result.setExciseDutyTypeId(materialMasterExtension.getExciseDutyTypeId());
        result.setTaxWarehouseRegistration(taxWarehouse.getTaxWarehouseRegistration());
        result.setEdMovementCategoryId(movementCategory.getId());

        result.setAlcoholicStrength(materialMasterExtension.getAlcoholicStrength());
        result.setTaxWarehouseRegistrationOfManufacturingPlant(materialMasterExtension.getTaxWarehouseRegistrationOfManufacturingPlant());
        result.setExciseDutyNumberForTaxWarehouse(materialMasterExtension.getExciseDutyNumberForTaxWarehouse());
        result.setExternalTaxWarehouseRegistration(shipToExtension.getExternalTaxWarehouseRegistration());
        result.setExternalExciseDutyNumber(shipToExtension.getExternalExciseDutyNumber());
        result.setExciseDutyProcurementIndicator(materialMasterExtension.getExciseDutyProcurementIndicator());

        return result;
    }

    protected TaxCalculationInput initTaxCalculationInput(S4WorklistItem worklistItem,
            MaterialMasterExtension materialMasterExtension, SettlementUnit settlementUnit,
            BusinessTransactionType businessTransactionType) {
        TaxCalculationInput result = new TaxCalculationInput();

        result.setMaterialNumber(worklistItem.getMaterialNumber());
        result.setMaterialMasterExtension(materialMasterExtension);
        result.setSettlementUnit(settlementUnit);
        result.setBusinessTransactionType(businessTransactionType);
        result.setMaterialDocumentQuantity(worklistItem.getQuantity());
        result.setMaterialDocumentBaseUnitOfMeasure(worklistItem.getBaseUnitOfMeasure());
        result.setMaterialDocumentPostingDate(worklistItem.getPostingDateInDocument());

        return result;

    }

    protected TaxWarehouse getTaxWarehouseRegistration(String plant, String sorageLocation, String excuseDutyTyeID) throws EntityNotFoundException {
        TaxWarehouseRepository repo = new TaxWarehouseRepository(handler);
        TaxWarehouseAssignment assigment = repo.getAssignmentByKey(plant, sorageLocation, excuseDutyTyeID, LocalDate.now());
        TaxWarehouse result = repo.getByKey(excuseDutyTyeID, assigment.getTaxWarehouseRegistration());
        return result;
    }

    protected List<MaterialMasterExtension> getMaterialMasterExtension(String materialNumber, String companyCode) {
        MaterialMasterExtensionRepository repo = new MaterialMasterExtensionRepository(handler);
        List<MaterialMasterExtension> result = repo.getByKey(materialNumber, companyCode);
        return result;
    }

    protected MovementCategory getMovementCategory(String erpMovementType, String erpMovementIndicator) throws MovementTypeUnknownException {
        MovementCategoryRepository repo = new MovementCategoryRepository(handler);
        MovementTypeMapping typeMapping = repo.getMovementTypeMappingByKey(erpMovementType, erpMovementIndicator);
        if (typeMapping == null || typeMapping.getEdMovementCategoryId().isEmpty()) {
            throw new MovementTypeUnknownException();
        }
        MovementCategory result = repo.getByKey(typeMapping.getEdMovementCategoryId());
        return result;
    }

    protected ShipToMasterExtension getShipToMasterExtension(String customerNumber, LocalDate date) throws EntityNotFoundException {
        ShipToMasterExtensionRepository repo = new ShipToMasterExtensionRepository(handler);
        ShipToMasterExtension result = repo.queryByKey(customerNumber, date);
        return result;
    }

    protected StockLedgerGroup getStockLedgerGroup(String id) {
        StockLedgerGroupRepository repo = new StockLedgerGroupRepository(handler);
        StockLedgerGroup result = repo.getByKey(id);
        return result;
    }

    protected BusinessTransactionType analyseShipToParty(ShipToMasterExtension shiptToMasterExtension) {
        // TODO: Differentiate between shipment within own country and within EU
        if (shiptToMasterExtension.getExternalTaxWarehouseRegistration() != null
                && !shiptToMasterExtension.getExternalTaxWarehouseRegistration().isEmpty()) {
            return BusinessTransactionType.con_steuerlager;
        }
        if (shiptToMasterExtension.isThirdCountryIndicator()) {
            return BusinessTransactionType.con_drittland;
        }
        return BusinessTransactionType.con_versteuert;
    }

    protected SettlementUnit getSettlementUnit(String companyCode, String exciseDutyTypeId) {
        SettlementUnitRepository repo = new SettlementUnitRepository(handler);
        SettlementUnit result = repo.getByKey(companyCode, exciseDutyTypeId);
        return result;
    }

    protected String getStockLedgerGroupId(BusinessTransactionType businessTransactionType, MovementCategory movementCategory)
            throws StockLedgerGroupMappingNotFound, StockLedgerGroupNotFoundException {
        // TODO: Has to be defined in a rules engine to do the proper mapping
        String stockLedgerGroupId = null;
        if (movementCategory.getId().equals("101")) {
            switch (businessTransactionType) {
                case con_steuerlager:
                    stockLedgerGroupId = "02040";
                    break;
                case con_versteuert:
                    stockLedgerGroupId = "02010";
                    break;
                case con_drittland:
                    stockLedgerGroupId = "02030";
                    break;
                case con_steuerlager_eu:
                    stockLedgerGroupId = "02060";
                    break;
                case con_eu:
                    stockLedgerGroupId = "02000";
                    break;
                default:
                    throw new StockLedgerGroupMappingNotFound();
            }
        }
        if (stockLedgerGroupId == null) {
            throw new StockLedgerGroupNotFoundException();
        }
        return stockLedgerGroupId;

    }

    // ----------------------------------------------------------------------------------------------------------------------------
    // additional input for extension point
    // ----------------------------------------------------------------------------------------------------------------------------
    private CustomerGroupSpecialPartnerAssignment getCustomerGroupSpecialPartnerAssignment(String kdgrp) {
        CDSQuery cdsQuery = new CDSSelectQueryBuilder(IExciseDutyEntities.MODEL_NAME + "." + "CustomerGroupSpecialPartnerAssignment")
                .where(new ConditionBuilder().columnName("customerGroup").EQ(kdgrp))
                .build();
        List<EntityData> queryResults;
        try {
            queryResults = ((CDSHandler) handler).executeQuery(cdsQuery).getResult();
        } catch (CDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }

        if (queryResults.isEmpty()) {
            return null;
        } else {
            EntityData queryResult = queryResults.get(0);
            Map<String, Object> resultMap = queryResult.asMap();

            CustomerGroupSpecialPartnerAssignment customerGroupSpecialPartnerAssignment = new CustomerGroupSpecialPartnerAssignment();
            customerGroupSpecialPartnerAssignment.setCustomerGroup((String) resultMap.get("customerGroup"));

            String specialPartnerTypeId = (String) resultMap.get("specialPartnerType");
            if (specialPartnerTypeId != null) {
                SpecialPartnerType specialPartnerType = getSpecialPartnerType(specialPartnerTypeId);
                customerGroupSpecialPartnerAssignment.setSpecialPartnerType(specialPartnerType);
            }
            return customerGroupSpecialPartnerAssignment;
        }
    }

    private SpecialPartnerType getSpecialPartnerType(String id) {
        CDSQuery cdsQuery = new CDSSelectQueryBuilder(IExciseDutyEntities.MODEL_NAME + "." + "SpecialPartnerType")
                .where(new ConditionBuilder().columnName("id").EQ(id))
                .build();
        List<EntityData> queryResults;
        try {
            queryResults = ((CDSHandler) handler).executeQuery(cdsQuery).getResult();
        } catch (CDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }
        if (queryResults.isEmpty()) {
            return null;
        } else {
            EntityData queryResult = queryResults.get(0);
            Map<String, Object> resultMap = queryResult.asMap();

            SpecialPartnerType specialPartnerType = new SpecialPartnerType();
            specialPartnerType.setId((String) resultMap.get("id"));
            specialPartnerType.setDescription((String) resultMap.get("description"));
            specialPartnerType.setTaxationRelevantIndicator((String) resultMap.get("taxationRelevantIndicator"));
            return specialPartnerType;
        }
    }

    private ShipToMasterExtensionEU getShipToMasterExtensionEU(String kunnr) {

        Map<String, Object> keys = new HashMap<>();
        keys.put("customerNumber", kunnr);

        List<String> flattenedElementNames = Arrays.asList(
                "customerNumber",
                "taxWarehouseRegistration",
                "validFrom",
                "validTo",
                "outsideEUTaxTerritoryIndicator",
                "euCountry1",
                "euCountry2");

        EntityData resultEntity;
        try {
            resultEntity = handler.executeRead(IExciseDutyEntities.MODEL_NAME + "." + "ShipToMasterExtensionEU", keys, flattenedElementNames);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }

        Map<String, Object> resultMap = resultEntity.asMap();

        ShipToMasterExtensionEU shipToMasterExtensionEU = new ShipToMasterExtensionEU();
        shipToMasterExtensionEU.setCustomerNumber((String) resultMap.get("customerNumber"));
        shipToMasterExtensionEU.setOutsideEUTaxTerritoryIndicator((Boolean) resultMap.get("outsideEUTaxTerritoryIndicator"));
        shipToMasterExtensionEU.setTaxWarehouseRegistration((String) resultMap.get("taxWarehouseRegistration"));
        shipToMasterExtensionEU.setValidFrom(dateToLocalDate((java.sql.Date) resultMap.get("validFrom")));
        shipToMasterExtensionEU.setValidTo(dateToLocalDate((java.sql.Date) resultMap.get("validTo")));

        String euCountry1Id = (String) resultMap.get("euCountry1");
        if (euCountry1Id != null) {
            Country euCountry1 = getCountry(euCountry1Id);
            shipToMasterExtensionEU.setEuCountry1(euCountry1);
        }

        String euCountry2Id = (String) resultMap.get("euCountry2");
        if (euCountry2Id != null) {
            Country euCountry2 = getCountry(euCountry2Id);
            shipToMasterExtensionEU.setEuCountry1(euCountry2);
        }

        return shipToMasterExtensionEU;
    }

    private LocalDate dateToLocalDate(Date o) {
        LocalDate localDate = null;
        if (o != null) {
            localDate = ((java.sql.Date) o).toLocalDate();
        }
        return localDate;
    }

    private Country getCountry(String land1) {

        Map<String, Object> keys = new HashMap<>();
        keys.put("countryKey", land1);

        List<String> flattenedElementNames = Arrays.asList(
                "countryKey",
                "europeanUnionIndicator");

        EntityData resultEntity;
        try {
            resultEntity = handler.executeRead(IExciseDutyEntities.MASTER_DATA_REPLICATION_MODEL + "." + "Countries", keys, flattenedElementNames);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }

        Map<String, Object> resultMap = resultEntity.asMap();

        Country country = new Country();
        country.setCountryKey((String) resultMap.get("countryKey"));
        country.setEuropeanUnionIndicator((Boolean) resultMap.get("europeanUnionIndicator"));

        return country;
    }
}
