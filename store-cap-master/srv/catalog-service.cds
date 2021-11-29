using my.company.Customer as Customer from '../db/customer';
using my.company.Product as Product from '../db/product';
using my.company.Transaction.Item as Item from '../db/transaction';
using my.company.Transaction.Header as Header from '../db/transaction';

service CatalogService {
    
    // Tech Note: Annotations after entity declaration
    entity Customers @(title: '{i18n>customerService}') 
    as projection on Customer;

    // Tech Note: Annotations before the entity declaration
    @(title: '{i18n>productService}') 
    entity Products as projection on Product;
    
    // Accessible through http://localhost:4004/catalog/hello(to='world')
    // The "function" command will generate a GET request on the Catalog services
    function hello (to:String) returns String;

}