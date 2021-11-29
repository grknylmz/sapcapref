using com.playground.bookshop.CatalogService from './bookshop-service';

// list report
annotate CatalogService.Books with @(
  UI:{
    SelectionFields: [title],
    LineItem: [
			{ Value: title, Label: 'title' },            
      { Value: stock, Label: 'stock' },
    ]
  }
);

//object page
annotate CatalogService.Books with @(
  UI.HeaderInfo: {
    Title: { Value: ID },
    Description: { Value: title },
  },

  UI.Facets: [
    {
      $Type:'UI.CollectionFacet',
      Facets:[
        { $Type: 'UI.ReferenceFacet', Label: 'Data',  Target: '@UI.FieldGroup#Data' },
        { $Type: 'UI.ReferenceFacet', Label: 'Admin',  Target: '@UI.FieldGroup#Admin' }
      ]
    }
  ],

  UI.FieldGroup#Data: {
    Label: 'Data',
    Data: [
      {Value: ID },
      {Value: title }
    ]
  },

	UI.FieldGroup#Admin: {
    Label: 'Admin',
    Data: [
      {Value: createdBy},
      {Value: createdAt},
      {Value: modifiedBy},
      {Value: modifiedAt}
    ]
  },

  UI.PresentationVariant: {
			SortOrder: [ {$Type: 'Common.SortOrderType', Property: ID, Descending: true} ]
	}
);