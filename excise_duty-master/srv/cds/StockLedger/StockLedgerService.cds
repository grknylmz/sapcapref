using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using MasterDataReplicationModel from '../../../db/cds/MasterDataReplicationModel';
using erpDataTypes from '../../../db/cds/erpDataTypes';
using edDataTypes from '../../../db/cds/edDataTypes';

using from './StockLedgerAnnotations';

service StockLedgerService
{

  type ExciseDutyCalculationResult{
    exciseDutyTypeId: edDataTypes.edTypeId;
    taxValueAmount: edDataTypes.taxValue;
    taxValueCurrency: erpDataTypes.WAERS;
  };

  type TaxReportingResult{
    exciseDutyTypeId: edDataTypes.edTypeId;
    alcoholicStrength : edDataTypes.alcoholicStrength;
    exciseDutyProcurementIndicator: edDataTypes.procurementType;
    taxRelevantAmount:edDataTypes.taxValue;
    notTaxRelevantAmount:edDataTypes.taxValue;
    thirdCoundryNotTaxRelevantAmount:edDataTypes.taxValue;
    taxValueCurrency: erpDataTypes.WAERS;
  };

  @Capabilities: { Insertable:false, Updatable:false, Deletable:false, }
  @Common.FilterExpressionRestrictions: [ {Property: materialDocumentPostingDate, AllowedExpressions: #SingleInterval} ]
  @sap.searchable: 'false'
  @sap.semantics: 'aggregate'
  entity StockLedgerLineItem as projection on ExciseDutyModel.StockLedgerLineItem;

  @Capabilities: { Insertable:false, Updatable:false, Deletable:false, }
  entity MaterialDescription as projection on MasterDataReplicationModel.MaterialDescription;

  @Capabilities: { Insertable:false, Updatable:false, Deletable:false, }
  entity MaterialMasterExtension as projection on ExciseDutyModel.MaterialMasterExtension{
    *
  }excluding{
    materialDescription,
    exciseDutyType
  };

  @Capabilities: { Insertable:false, Updatable:false, Deletable:false, }
  entity Customer as projection on MasterDataReplicationModel.Customer;
  
  // Value Help
  @Capabilities: { Insertable:false, Updatable:false, Deletable:false }
  @sap.searchable: 'false'
  entity ExciseDutyType as projection on ExciseDutyModel.ExciseDutyType
   {
    id,
    description
   };

  @Capabilities: { Insertable:false, Updatable:false, Deletable:false, }
  entity TaxWarehouse as projection on ExciseDutyModel.TaxWarehouse{
    *
  }excluding{
    exciseDutyType,
    taxWarehouseAssignments
  };
  
  entity Countries as projection on MasterDataReplicationModel.Countries;
  
  action ProcessWorklist(
  ) returns String(255);
  action ProcessSingleWorklistItem(
      materialDocumentYear: erpDataTypes.MJAHR,
      materialDocumentNumber: erpDataTypes.MBLNR,
      materialDocumentItem: erpDataTypes.MBLPO
  ) returns String(255);
  action ProduceWorklistForEventing(
  ) returns String(255);

  action ExciseDutyCalculation(
    materialNumber: erpDataTypes.MATNR,
    companyCode: erpDataTypes.BUKRS,
    plant: erpDataTypes.WERKS_D,
    customerNumber: erpDataTypes.KUNNR,
    quantity: erpDataTypes.MENGE_D,
    baseUnitOfMeasure: erpDataTypes.UNIT,
    postingDate: erpDataTypes.BUDAT
   ) returns array of ExciseDutyCalculationResult;

  function TaxReporting(
    taxWarehouseRegistration: edDataTypes.taxWarehouseRegistration,
    fromDate: Date,
    toDate: Date
  ) returns array of TaxReportingResult;
}