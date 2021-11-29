using {sap.krc.bookshop as my} from '../db/schema';

@path : '/browse'
@impl : './my-service.js'
service CatalogService {

    @readonly
    entity Books  as

        select from my.Books {
            *,
            author.name as author
        }
        excluding {
            createdBy,
            modifiedBy
        };
    /*
     select from my.Books {
*
}
excluding {
createdBy,
modifiedBy
};
*/

    @requires_ : 'authenticated-user'
    @insertonly
    entity Orders as projection on my.Orders;
}
