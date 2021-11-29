using c4u.flp.sample as service from '../db/model';

service CatalogService {
    entity Books as projection on service.Books;
}

