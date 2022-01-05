using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';

using from './StockLedgerCharacteristicsAnnotations';

service StockLedgerCharacteristicsService
{

  entity StockLedgerGroup as projection on ExciseDutyModel.StockLedgerGroup;

  entity StockLedgerDivisionValueHelp as projection on ExciseDutyModel.StockLedgerDivisionValueHelp;
  
  entity StockLedgerSubdivisionValueHelp as projection on ExciseDutyModel.StockLedgerSubdivisionValueHelp;
  
  entity MovementEntryBehaviorValueHelp as projection on ExciseDutyModel.MovementEntryBehaviorValueHelp;

}