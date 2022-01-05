using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';

using from './StockLedgerProcessingErrorAnnotations';

service StockLedgerProcessingErrorService
{

  @Capabilities: { Insertable:false, Updatable:false, Deletable:false, }
  @sap.searchable: 'false'
  entity StockLedgerProcessingError as projection on ExciseDutyModel.StockLedgerProcessingError {
    *
  } actions {
    action ReprocessItem( 
    ) returns String(255);
  };
  
  
  

}