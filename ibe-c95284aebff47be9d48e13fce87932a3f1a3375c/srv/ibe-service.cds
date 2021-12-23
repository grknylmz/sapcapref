using { sap.capire.ibe as my } from '../db/schema';
//test comment
service ibeService {
  entity ibe as projection on my.ibeReq;
}
