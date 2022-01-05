using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using from './ExciseDutyTypeAnnotations';

service ExciseDutyTypeService
{
  @sap.searchable: 'false' //ToDo: Add to grunfile.js as cdsv does not yet support this (https://github.wdf.sap.corp/CDS/cdsv/issues/339)
  entity ExciseDutyType as projection on ExciseDutyModel.ExciseDutyType;

  entity SettlementUnit as projection on ExciseDutyModel.SettlementUnit;
  
  entity ContainerContentCalculationIndicatorValueHelp as projection on ExciseDutyModel.ContainerContentCalculationIndicatorValueHelp;

}