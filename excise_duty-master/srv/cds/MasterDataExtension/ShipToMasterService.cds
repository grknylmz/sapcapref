using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using MasterDataReplicationModel from '../../../db/cds/MasterDataReplicationModel';
using API_BUSINESS_PARTNER as bp from '../remote/API_BUSINESS_PARTNER';

using from './ShipToMasterAnnotations';

service ShipToMasterService
{

  entity ShipToMasterExtension as projection on ExciseDutyModel.ShipToMasterExtension;

  entity SpecialPartnerType as projection on ExciseDutyModel.SpecialPartnerType;

  entity Customer as projection on MasterDataReplicationModel.Customer;

  //API_BUSINESS_PARTNER	
  @cds.persistence.skip
	entity A_Customer as projection on bp.A_CustomerType{
    Customer,
    CustomerName
  };

}