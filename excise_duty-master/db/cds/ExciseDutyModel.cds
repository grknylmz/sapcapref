using erpDataTypes from './erpDataTypes';
using edDataTypes from './edDataTypes';

using MasterDataReplicationModel from './MasterDataReplicationModel';

@Common.Label: "Excise Duty"
@Analytics.visible: true
context ExciseDutyModel
{

    // ED Configuration
    @Common.Label: "Excise Duty Type"
    @Analytics.visible: true
    entity ExciseDutyType{
        key id: edDataTypes.edTypeId; 
            description: edDataTypes.edDescription; // ToDo: Has to be language dependenent
            containerContentCalculationIndicator: edDataTypes.containerContentCalculationIndicator; // /BEV2/EDATTRGEF
            containerContentUnit : erpDataTypes.UNIT;
            containerContentDecimalPlaces : Integer; //ToDo Validation that it is lower than 10

            settlementUnits: Association[*] to SettlementUnit on settlementUnits.exciseDutyTypeId = id;
    }

    @Common.Label: "Tax Warehouse"
    @Analytics.visible: true
    entity TaxWarehouse{
        key exciseDutyTypeId: edDataTypes.edTypeId;
        key taxWarehouseRegistration: edDataTypes.taxWarehouseRegistration;
            validFrom: Date not null;
            validTo: Date;
            description: edDataTypes.edDescription; // ToDo: Has to be language dependenent
            companyCode: erpDataTypes.BUKRS;
            useStockLedgerSubdivisions:Boolean;
            stockLedgerType:edDataTypes.stockLedgerWarehouseType;

            exciseDutyType: Association[1] to ExciseDutyType on exciseDutyType.id = exciseDutyTypeId;
            taxWarehouseAssignments: Association[*] to TaxWarehouseAssignment on taxWarehouseAssignments.exciseDutyTypeId = exciseDutyTypeId AND taxWarehouseAssignments.taxWarehouseRegistration = taxWarehouseRegistration;
            country: Association to MasterDataReplicationModel.Countries;
    }

    @Common.Label: "Settlement Unit"
    @Analytics.visible: true
    entity SettlementUnit{ // /BEV2/ED915
        key companyCode: erpDataTypes.BUKRS;
        key exciseDutyTypeId: edDataTypes.edTypeId;
            settlementUnit: erpDataTypes.UNIT;
            baseQuantityUnit: erpDataTypes.UNIT;
            volumeDecimalPlaces : Integer; //ToDo Validation that it is lower than 10
    }

    @Common.Label: "Tax Warehouse Assignment"
    @Analytics.visible: true
    entity TaxWarehouseAssignment{
        key plant: erpDataTypes.WERKS_D;
        key storageLocation: erpDataTypes.LGORT_D;
        key exciseDutyTypeId: edDataTypes.edTypeId;
        key validFrom: Date;
            validTo: Date;
            taxWarehouseRegistration: edDataTypes.taxWarehouseRegistration;
            taxWarehouse: Association[1] to TaxWarehouse on taxWarehouse.taxWarehouseRegistration = taxWarehouseRegistration AND taxWarehouse.exciseDutyTypeId = exciseDutyTypeId ; // TODO: Check if this is working on HANA

    }

    @Common.Label: "Movement Category"
    @Analytics.visible: true
    entity MovementCategory {
        key id: edDataTypes.movementCategory;
            description: edDataTypes.edLongDescription;
            
            movementTypeMappings: Association[*] to MovementTypeMapping on movementTypeMappings.edMovementCategoryId = id;
    }

    @Common.Label: "Movement Type Mapping"
    @Analytics.visible: true
    entity MovementTypeMapping { // /BEV2/ED906
        key erpMovementType: erpDataTypes.BWART;
        key erpMovementIndicator: erpDataTypes.KZBEW;
            edMovementCategoryId: edDataTypes.movementCategory not null;

        edMovementCategory: Association[1] to MovementCategory on edMovementCategory.id = edMovementCategoryId;
    }


    // Stock Ledger Definition
    @Common.Label: "Stock Ledger Group"
    @Analytics.visible: true
    entity StockLedgerGroup { // /BEV2/ED907K and /BEV2/ED907
        key id: edDataTypes.stockLedgerGroupId;
            description: edDataTypes.edDescription;
            stockLedgerDivision: edDataTypes.stockLedgerDivision;
            stockLedgerSubdivision: edDataTypes.stockLedgerSubdivision;
            movementEntryBehavior: edDataTypes.movementEntryBehavior;

            stockLedgerDivisionValueHelp: Association[1] to StockLedgerDivisionValueHelp on stockLedgerDivisionValueHelp.id = stockLedgerDivision;
            stockLedgerSubdivisionValueHelp: Association[1] to StockLedgerSubdivisionValueHelp on stockLedgerSubdivisionValueHelp.id = stockLedgerSubdivision;
            movementEntryBehaviorValueHelp: Association[1] to MovementEntryBehaviorValueHelp on movementEntryBehaviorValueHelp.id = movementEntryBehavior;

    }

    @Common.Label: "Stock Ledger Line Item"
    @Analytics.visible: true
    entity StockLedgerLineItem { // /BEV2/EDMSE
        @Common.Label: "Material Document Year"
        key materialDocumentYear: erpDataTypes.MJAHR;
        @Common.Label: "Material Document Number"
        key materialDocumentNumber: erpDataTypes.MBLNR;
        @Common.Label: "Material Document Item"
        key materialDocumentItem: erpDataTypes.MBLPO; // Item in Material Document
        @Common.Label: "Excise Duty Position Number"
        key exciseDutyPositionNumber: edDataTypes.edPositionNumber; // Item Number
        @Common.Label: "Excise Duty Type"
        key exciseDutyTypeId: edDataTypes.edTypeId;
            @Common.Label: "Company"
            companyCode: erpDataTypes.BUKRS;
            @Common.Label: "Tax Warehouse Registration"
            taxWarehouseRegistration: edDataTypes.taxWarehouseRegistration;
            @Common.Label: "Plant"
            plant: erpDataTypes.WERKS_D;
            @Common.Label: "Storage Location"
            storageLocation: erpDataTypes.LGORT_D;
            @Common.Label: "Material Document Posting Date"
            materialDocumentPostingDate: erpDataTypes.BUDAT; //Posting Date in the Document
            @Common.Label: "Stock Ledger Number"
            stockLedgerNumber: edDataTypes.stockLedgerNumber;
            @Common.Label: "Extended Stock Ledger Relevance Indicator"
            noEntryInExtendedStockLedger: edDataTypes.noEntryInExtendedStockLedger;
            @Common.Label: "Material Number"
            materialNumber: erpDataTypes.MATNR;
            @Common.Label: "Batch Number"
            batchNumber: erpDataTypes.CHARG_D;
            @Common.Label: "Quantity"
            quantity: erpDataTypes.MENGE_D;
            @Common.Label: "Base Unit of Measure"
            baseUnitOfMeasure: erpDataTypes.MEINS;
            @Common.Label: "Movement Category"
            edMovementCategoryId: edDataTypes.movementCategory;
            @Common.Label: "ERP Movement Type"
            erpMovementType: erpDataTypes.BWART; //ToDo: Clarify how to deal with such redundant fields; assumption: they are kept due to missing time dependency concept?
            @Common.Label: "Stock Ledger Group"
            stockLedgerGroupId: edDataTypes.stockLedgerGroupId;
            @Common.Label: "Stock Ledger Division"
            stockLedgerDivision: edDataTypes.stockLedgerDivision;
            @Common.Label: "Stock Ledger Subdivision"
            stockLedgerSubdivision: edDataTypes.stockLedgerSubdivision;
            @Common.Label: "Alcoholic Strength"
            alcoholicStrength : edDataTypes.alcoholicStrength;
            @Common.Label: "Tax Warehouse Registration of Manufacturing Plant"
            taxWarehouseRegistrationOfManufacturingPlant: edDataTypes.taxWarehouseRegistration;
            @Common.Label: "Tax Warehouse"
            exciseDutyNumberForTaxWarehouse: edDataTypes.exciseDutyNumber;
            @Common.Label: "External Tax Warehouse Registration"
            externalTaxWarehouseRegistration: edDataTypes.taxWarehouseRegistration;
            @Common.Label: "External Excise Duty Number"
            externalExciseDutyNumber: edDataTypes.exciseDutyNumber;
            @Common.Label: "sales Order Number"
            salesOrderNumber: erpDataTypes.AUFNR;
            @Common.Label: "Sales Order Item"
            salesOrderItem: erpDataTypes.POSNR;
            @Common.Label: "Procurement Indicator"
            exciseDutyProcurementIndicator: edDataTypes.procurementType;
            @Common.Label: "Accounting Journal Reference"
            accountingJournalReference: String(18);
            @Common.Label: "Tax Value"
            taxValueAmount: edDataTypes.taxValue;
            @Common.Label: "Currency"
            taxValueCurrency: erpDataTypes.WAERS;
            @Common.Label: "Customer Number"
            customerNumber: erpDataTypes.KUNNR;

            materialDescription: Association [1] to MasterDataReplicationModel.MaterialDescription on materialDescription.materialNumber = materialNumber;
            materialMasterExtension: Association [1] to MaterialMasterExtension on materialMasterExtension.materialNumber = materialNumber AND materialMasterExtension.companyCode = companyCode AND materialMasterExtension.exciseDutyTypeId = exciseDutyTypeId;
            customerData: Association [1] to MasterDataReplicationModel.Customer on customerData.customerNumber = customerNumber;
            exciseDutyType: Association[1] to ExciseDutyType on exciseDutyType.id = exciseDutyTypeId;
            taxWarehouse: Association[1] to TaxWarehouse on taxWarehouse.taxWarehouseRegistration = taxWarehouseRegistration AND taxWarehouse.exciseDutyTypeId = exciseDutyTypeId ;

    }

    @Common.Label: "Tax Rate"
    @Analytics.visible: true
    entity TaxRate { // /BEV2/ED918
        key country: erpDataTypes.LAND1;
        key exciseDutyTypeId: edDataTypes.edTypeId;
        key baseQuantityUnit: erpDataTypes.UNIT; //ToDo: Why is it possible to declare multiple BQUs for the same ED Type? 
        key validFrom: Date;
        key alcoholicStrengthLowerLimit: edDataTypes.alcoholicStrength;
            taxRate: edDataTypes.taxRate;
            currency: erpDataTypes.WAERS;

        exciseDutyType: Association[1] to ExciseDutyType on exciseDutyType.id = exciseDutyTypeId;
    }


    // Master Data
    @Common.Label: "Material Extension"
    @Analytics.visible: true
    entity MaterialMasterExtension{
        key materialNumber: erpDataTypes.MATNR;
        key companyCode: erpDataTypes.BUKRS;
        key exciseDutyTypeId: edDataTypes.edTypeId;
            exciseDutyProcurementIndicator: edDataTypes.procurementType;
            taxWarehouseRegistrationOfManufacturingPlant: edDataTypes.taxWarehouseRegistration;
            exciseDutyNumberForTaxWarehouse: edDataTypes.exciseDutyNumber;
            exciseDutyTypeIndependentMaterialGroup: edDataTypes.exciseDutyTypeIndependentMaterialGroup;
            alcoholicStrength : edDataTypes.alcoholicStrength;
            validFrom: Date;

            materialDescription: Association [1] to MasterDataReplicationModel.MaterialDescription on materialDescription.materialNumber = materialNumber;
            exciseDutyType: Association[1] to ExciseDutyType on exciseDutyType.id = exciseDutyTypeId;
    }

    @Common.Label: "Ship-to Extension"
    @Analytics.visible: true
    entity ShipToMasterExtension{ // /BEV/EDWEMPF
        key customerNumber: erpDataTypes.KUNNR;
        // key exciseDutyTypeId: edDataTypes.edTypeId;
        // key companyCode: erpDataTypes.BUKRS;
        // key taxWarehouseRegistration: edDataTypes.taxWarehouseRegistration;
        key validFrom: Date;
            validTo: Date;
            externalTaxWarehouseRegistration: edDataTypes.taxWarehouseRegistration; //EDSLAGERE
            externalExciseDutyNumber: edDataTypes.exciseDutyNumber; //EDVBSNRE
            exciseDutySpecialPartnerTypeId: edDataTypes.exciseDutySpecialPartnerTypeId;
            thirdCountryIndicator: Boolean; // EDEXPORT
            registeredConsignee: edDataTypes.registeredConsignee;

            exciseDutySpecialPartnerType: Association [1] to SpecialPartnerType on exciseDutySpecialPartnerType.id = exciseDutySpecialPartnerTypeId;
            customerData: Association [1] to MasterDataReplicationModel.Customer on customerData.customerNumber = customerNumber;
    }

    @Common.Label: "Ship-to Extension (EU)"
    @Analytics.visible: true
    entity ShipToMasterExtensionEU{ // /BEV/EDWEMPF_EU
        key customerNumber: erpDataTypes.KUNNR;
        // key exciseDutyTypeId: edDataTypes.edTypeId;
        // key companyCode: erpDataTypes.BUKRS;
        key taxWarehouseRegistration: edDataTypes.taxWarehouseRegistration;
        key validFrom: Date;
            validTo: Date;
            outsideEUTaxTerritoryIndicator: edDataTypes.outsideEUTaxTerritoryIndicator;
            euCountry1: erpDataTypes.LAND1; // /BEV2/EDEUDOM4COUNTRY
            euCountry2: erpDataTypes.LAND1; // /BEV2/EDEUDOM4COUNTRY
           
    }

    @Common.Label: "Special Partner Type"
    @Analytics.visible: true
    entity SpecialPartnerType{ // /BEV/ED961
        key id : edDataTypes.exciseDutySpecialPartnerTypeId;
            taxationRelevantIndicator: Boolean;
            description : edDataTypes.edDescription;
    }

    @Common.Label: "Customer Group Special Partner Assignment"
    @Analytics.visible: true
    entity CustomerGroupSpecialPartnerAssignment{ // /BEV/ED963
        key customerGroup: erpDataTypes.KDGRP;
            specialPartnerType: Association to SpecialPartnerType;
    }

    @Common.Label: "Container Content Calculation Indicator Description"
    @Analytics.visible: true
    entity ContainerContentCalculationIndicatorValueHelp {
        key id : edDataTypes.containerContentCalculationIndicator;
            description: edDataTypes.edDescription;
    }

    @Common.Label: "Stock Ledger Division Description"
    @Analytics.visible: true
    entity StockLedgerDivisionValueHelp {
         key id : edDataTypes.stockLedgerDivision;
             description: edDataTypes.edDescription;
    }

    @Common.Label: "Stock Ledger Subdivision Description"
    @Analytics.visible: true
    entity StockLedgerSubdivisionValueHelp {
         key id : edDataTypes.stockLedgerDivision;
             stockLedgerDivision: edDataTypes.stockLedgerDivision;
             description: edDataTypes.edLongDescription;
    }

    @Common.Label: "Movement Entry Behavior Description"
    @Analytics.visible: true
    entity MovementEntryBehaviorValueHelp {
         key id : edDataTypes.movementEntryBehavior;
             description: edDataTypes.edDescription;
    }

    @Analytics.hidden: true
    entity StockLedgerProcessingError {
        key materialDocumentYear: erpDataTypes.MJAHR;
        key materialDocumentNumber: erpDataTypes.MBLNR;
        key materialDocumentItem: erpDataTypes.MBLPO; // Item in Material Document
        key exciseDutyPositionNumber: edDataTypes.edPositionNumber; // Item Number
            //Adding some non-key fields to allow for contextual navigation (TODO: clarify where we want to store these actually)
            companyCode: erpDataTypes.BUKRS;
            plant: erpDataTypes.WERKS_D;
            storageLocation: erpDataTypes.LGORT_D;
            materialDocumentPostingDate: erpDataTypes.BUDAT; //Posting Date in the Document
            materialNumber: erpDataTypes.MATNR;

            errorText: edDataTypes.edLongDescription;
    }

    @Analytics.hidden: true
    entity ExtensionFunctionConfig {
        key exciseDutyTypeId: edDataTypes.edTypeId;
        key extensionPoint: edDataTypes.extensionPoint;
            extensionDestination: edDataTypes.extensionDestination;
    }

    @Analytics.hidden: true
    entity ExtensionPoint {
        key extensionPoint: edDataTypes.extensionPoint;
            description: edDataTypes.edLongDescription;
    }
};