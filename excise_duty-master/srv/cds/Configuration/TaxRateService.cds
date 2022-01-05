using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using from './TaxRateAnnotations';

service TaxRateService
{

  entity TaxRate as projection on ExciseDutyModel.TaxRate;
  
  // Value Help
  @Capabilities: { Insertable:false, Updatable:false, Deletable:false }
  @sap.searchable: 'false'
  entity ExciseDutyType as projection on ExciseDutyModel.ExciseDutyType
   {
    id,
    description
   };

}