using { sap.common } from '../db/data-model';
using SFLIGHT_CV from '../db/data-model';
using SBOOK_CV from '../db/data-model';

service CatalogService {
  @readonly
  entity SFLIGHT as projection on SFLIGHT_CV; 
  @readonly
  entity SBOOK as projection on SBOOK_CV; 
}

