using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using from './TaxWarehouseAnnotations';
using MasterDataReplicationModel from '../../../db/cds/MasterDataReplicationModel';

service TaxWarehouseService
{

  //Tax Warehouse
  entity TaxWarehouse as projection on ExciseDutyModel.TaxWarehouse;
  
  entity TaxWarehouseAssignment as projection on ExciseDutyModel.TaxWarehouseAssignment;
  
  // Value Help
  @Capabilities: { Insertable:false, Updatable:false, Deletable:false }
  @sap.searchable: 'false'
  entity ExciseDutyType as projection on ExciseDutyModel.ExciseDutyType
   {
    id,
    description
   };

  entity Countries as projection on MasterDataReplicationModel.Countries;

}