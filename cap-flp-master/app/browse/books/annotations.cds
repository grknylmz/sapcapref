using CatalogService as service from '../../../srv/service';

annotate service.Books with @( 
    UI : {
        LineItem : [
            {Value : ID, Label : 'ID'},
            {Value : title, Label : 'Title'}
        ]
    }
);
