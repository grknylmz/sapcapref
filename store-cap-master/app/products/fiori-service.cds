using my.company.Product as Product from '../../srv/catalog-service';

annotate Product with @(

    UI: {

        // Main Screen
        SelectionFields: [ name, segment ],
        LineItem: [
            {Value: name},   // Get label from the artifact cds
		    {Value: segment} // Get label from the artifact cds
	    ],
        HeaderInfo: {
			TypeName: 'Product',
			TypeNamePlural: 'Products',
            Title: { Value: name },
			Description: { Value: segment }
		},

        // Details Header
        HeaderFacets: [
	        {$Type: 'UI.ReferenceFacet', Label: '{i18n>productDetails}', Target: '@UI.FieldGroup#DetailsHeader'}
	    ],
        FieldGroup#DetailsHeader: {
    		Data: [
                {Value: ID, Label: 'Identification Code'},
			    {Value: name, Label: 'Product Name'},
		    ]
	    },

        // Details Screen
        Facets: [
		    {$Type: 'UI.ReferenceFacet', Label: 'Product Details', Target: '@UI.FieldGroup#ProductDetails'}
	    ],
        FieldGroup#ProductDetails: {
    		Data: [
			    {Value: ID        , Label: 'Identification Code'},
                {Value: name}     , // Get label from the artifact cds
			    {Value: segment } , // Get label from the artifact cds
			    {Value: price     , Label: '{i18n>product.price}'},
			    {Value: currency  , Label: 'Currency'}
		    ]
	    }       
    }
);

// It is possible to set annotation to the artifact 
// attributes through the following statement
annotate Product with {
    ID @title:'Identification Code' @UI.HiddenFilter;
    segment @title: '{i18n>product.segment}';
}