using my.company.Product as Product from '../db/product';
using my.company.Transaction.Item as Item from '../db/transaction';
using my.company.Transaction.Header as Header from '../db/transaction';

// The "path" annotation is not required but can
// be defined to accomplish custom requirements
service TransactionService @(path:'/transaction') {

    // Constraints resolved through annotation
    entity Headers @(
        title: '{i18n>headerService}', 
        // Capabilities are equivalent to annotation usage
        Capabilities: {
            insertRestriction: {Insertable: true},
            UpdateRestriction: {Updatable: false},
            DeleteRestriction: {Deletable: true}
        }
    ) as projection on Header;

    // Transaction Items with a single "read only" annotation
    @readonly
    entity Items as projection on Item;

    // It is possible to declare custom services through the "action" command
    // The action will generate a POST request on the backend transaction service
    @requires_: 'authenticated-user'
    action addItem (transaction: Header.ID, product: Product.ID);

}
