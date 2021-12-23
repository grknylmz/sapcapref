using my.bookshop as my from '../db/data-model';
using { metadata as external} from './external/metadata.csn';

service CatalogService {
    @readonly entity Books as projection on my.Books;

    @readonly entity BusinessPartners as projection on external.A_BusinessPartner {
      Key BusinessPartner as ID,
     FirstName,
     MiddleName,
     LastName,
     BusinessPartnerIsBlocked
    };

    // This will be non persisted as it is from real service. Want to fetch real data.  
    @cds.persistence.skip
    entity BusinessPartnersReal as projection on external.A_BusinessPartner {
            BusinessPartner, LastName, FirstName
    };
 
}