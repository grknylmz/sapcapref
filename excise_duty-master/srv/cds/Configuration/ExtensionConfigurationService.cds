using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using from './ExtensionConfigurationAnnotations';

service ExtensionConfigurationService
{
  @sap.searchable: 'false'
  entity ExtensionFunctionConfig as projection on ExciseDutyModel.ExtensionFunctionConfig;

  @Capabilities: { Insertable:false, Updatable:false, Deletable:false }
  @sap.searchable: 'false'
  entity ExtensionPoint as projection on ExciseDutyModel.ExtensionPoint;

  // Value Help
  @Capabilities: { Insertable:false, Updatable:false, Deletable:false }
  @sap.searchable: 'false'
  entity ExciseDutyType as projection on ExciseDutyModel.ExciseDutyType
   {
    id,
    description
   };

}