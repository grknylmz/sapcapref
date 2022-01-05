using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using MasterDataReplicationModel from '../../../db/cds/MasterDataReplicationModel';

using from './StockLedgerAlpAnnotations';

service StockLedgerAlpService
{

  @Capabilities: { Insertable:false, Updatable:false, Deletable:false, }
  @Common.FilterExpressionRestrictions: [ {Property: materialDocumentPostingDate, AllowedExpressions: #SingleInterval} ]
  @sap.searchable: 'false'
  @sap.semantics: 'aggregate'
  @Aggregation.ApplySupported.PropertyRestrictions: true
  entity StockLedgerLineItem as projection on ExciseDutyModel.StockLedgerLineItem {
	@Analytics.Dimension: true
	materialDocumentYear,
	@Analytics.Dimension: true
	materialDocumentNumber,
	@Analytics.Dimension: true
	materialDocumentItem,
	@Analytics.Dimension: true
	exciseDutyPositionNumber,
	@Analytics.Dimension: true
	exciseDutyTypeId,
	@Analytics.Dimension: true
	companyCode,
	@Analytics.Dimension: true
	taxWarehouseRegistration,
	@Analytics.Dimension: true
	plant,
	@Analytics.Dimension: true
	storageLocation,
	@Analytics.Dimension: true
	materialDocumentPostingDate,
	@Analytics.Dimension: true
	stockLedgerNumber,
	@Analytics.Dimension: true
	noEntryInExtendedStockLedger,
	@Analytics.Dimension: true
	materialNumber,
	@Analytics.Dimension: true
	batchNumber,
	@Analytics.Dimension: true
	baseUnitOfMeasure,
	@Analytics.Measure: true
	@Aggregation.default: #SUM
	@Measures.Unit: "baseUnitOfMeasure"
	quantity,
	@Analytics.Dimension: true
	edMovementCategoryId,
	@Analytics.Dimension: true
	erpMovementType,
	@Analytics.Dimension: true
	stockLedgerGroupId,
	@Analytics.Dimension: true
	stockLedgerDivision,
	@Analytics.Dimension: true
	stockLedgerSubdivision,
	@Analytics.Dimension: true
	alcoholicStrength,
	@Analytics.Dimension: true
	taxWarehouseRegistrationOfManufacturingPlant,
	@Analytics.Dimension: true
	exciseDutyNumberForTaxWarehouse,
	@Analytics.Dimension: true
	externalTaxWarehouseRegistration,
	@Analytics.Dimension: true
	externalExciseDutyNumber,
	@Analytics.Dimension: true
	taxValueCurrency,
	@Analytics.Measure: true
	@Aggregation.default: #SUM
	@Measures.ISOCurrency: "taxValueCurrency"
	taxValueAmount,
	@Analytics.Dimension: true
	salesOrderNumber,
	@Analytics.Dimension: true
	salesOrderItem,
	@Analytics.Dimension: true
	customerNumber
  }excluding{
	materialDocumentYear,
	materialDocumentNumber,
	materialDocumentItem,
	exciseDutyPositionNumber ,
	exciseDutyTypeId,
	companyCode,
	taxWarehouseRegistration,
	plant,
	storageLocation,
	materialDocumentPostingDate,
	stockLedgerNumber,
	noEntryInExtendedStockLedger,
	materialNumber,
	batchNumber,
	baseUnitOfMeasure,
	quantity,
	edMovementCategoryId,
	erpMovementType,
	stockLedgerGroupId,
	stockLedgerDivision,
	stockLedgerSubdivision,
	alcoholicStrength,
	taxWarehouseRegistrationOfManufacturingPlant,
	exciseDutyNumberForTaxWarehouse,
	externalTaxWarehouseRegistration,
	externalExciseDutyNumber,
	taxValueCurrency,
	taxValueAmount,
	salesOrderNumber,
	salesOrderItem,
	customerNumber
  };
  
  entity TaxWarehouse as projection on ExciseDutyModel.TaxWarehouse{
    *
  }excluding{
    exciseDutyType,
    taxWarehouseAssignments
  };

  

  entity Countries as projection on MasterDataReplicationModel.Countries;
  
  entity StockLedgerDivisionValueHelp as projection on ExciseDutyModel.StockLedgerDivisionValueHelp;
  
}
