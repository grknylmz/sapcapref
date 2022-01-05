using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using MasterDataReplicationModel from '../../../db/cds/MasterDataReplicationModel';
using API_PRODUCT_SRV as product from '../remote/API_PRODUCT_SRV';

using from './MaterialMasterAnnotations';

service MaterialMasterService
{

  entity MaterialMasterExtension as projection on ExciseDutyModel.MaterialMasterExtension;
  
  // Value Help
  @Capabilities: { Insertable:false, Updatable:false, Deletable:false }
  @sap.searchable: 'false'
  entity ExciseDutyType as projection on ExciseDutyModel.ExciseDutyType
   {
    id,
    description
   };

  entity MaterialDescription as projection on MasterDataReplicationModel.MaterialDescription;
  
  // Value Help
  @Capabilities: { Insertable:false, Updatable:false, Deletable:false }
  @sap.searchable: 'true'
  @cds.persistence.skip
  entity A_Product as projection on product.A_ProductType{
    Product
  };
  @Capabilities: { Insertable:false, Updatable:false, Deletable:false }
  @sap.searchable: 'true'
  @cds.persistence.skip
  entity A_ProductDescription as projection on product.A_ProductDescriptionType;
}