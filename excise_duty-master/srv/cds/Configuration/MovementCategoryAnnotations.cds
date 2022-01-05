using MovementCategoryService from './MovementCategoryService'; 

annotate MovementCategoryService.MovementCategory with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly;
	description
		@Common.Label: 'Description';
};

// List Report columns

annotate MovementCategoryService.MovementCategory with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: id},
		{$Type: 'UI.DataField', Value: description},
	],
	
// List report filters
	
	UI.SelectionFields: [ id ],
	
// Object page header
	
	UI.HeaderInfo: {
		Title: { Value: description },
		TypeName:'Movement Category', 
        TypeNamePlural:'Movement Categories'
	},

// Object page facets

	UI.Facets:
	[
		{
			$Type:'UI.CollectionFacet', 
			Facets: [
						{ $Type:'UI.ReferenceFacet', Label: 'General Info', Target: '@UI.Identification' },
						{ $Type:'UI.ReferenceFacet', Label: 'Values', Target: '@UI.FieldGroup#Values' }
					],
			Label:'Movement Category Details', //anchor title		
		},
		{
			$Type:'UI.ReferenceFacet',
			Label: 'Movement Type Mappings',
			Target: 'movementTypeMappings/@UI.LineItem'
		},
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: id}
	],

// Object page field groups
	
	UI.FieldGroup#Values: {
		Data: [
			{$Type: 'UI.DataField', Value: description},
		]
	}

);

annotate MovementCategoryService.MovementTypeMapping with {
	erpMovementType
		@Common.Label: 'Movement Type'
       	@Common.FieldControl: #ReadOnly;
	erpMovementIndicator
		@Common.Label: 'Movement Indicator'
       	@Common.FieldControl: #ReadOnly;
	edMovementCategoryId
		@Common.Label: 'Category';
};

annotate MovementCategoryService.MovementTypeMapping with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: erpMovementType},
		{$Type: 'UI.DataField', Value: erpMovementIndicator}
	],

// Object page header

	UI.HeaderInfo: {
		TypeName:'Movement Type Mapping', 
        TypeNamePlural:'Movement Type Mappings'
	},
	
	// Object page facets

	UI.Facets:
	[
		{
			$Type:'UI.CollectionFacet', 
			Facets: [
						{ $Type:'UI.ReferenceFacet', Label: 'General Info', Target: '@UI.Identification' }
					],
			Label:'Movement Type Mapping Details', //anchor title		
		},
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: erpMovementType},
		{$Type: 'UI.DataField', Value: erpMovementIndicator}
	],

);