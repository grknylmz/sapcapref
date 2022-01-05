using StockLedgerCharacteristicsService from './StockLedgerCharacteristicsService'; 

annotate StockLedgerCharacteristicsService.StockLedgerDivisionValueHelp with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly
		@Common.Text: "description";
	description
		@Common.Label: 'Description';
};

annotate StockLedgerCharacteristicsService.StockLedgerSubdivisionValueHelp with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly
		@Common.Text: "description";
	description
		@Common.Label: 'Description';
};

annotate StockLedgerCharacteristicsService.MovementEntryBehaviorValueHelp with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly
		@Common.Text: "description";
	description
		@Common.Label: 'Description';
};

annotate StockLedgerCharacteristicsService.StockLedgerGroup with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly;
	description
		@Common.Label: 'Description';
	stockLedgerDivision
		@Common.Label: 'Stock Ledger Division'
		@sap.value.list: 'fixed-values'
		@Common.Text: {
			"$value": "stockLedgerDivisionValueHelp/description", 
			"@UI.TextArrangement": #TextFirst
		}
		@Common.ValueList: {
			CollectionPath: 'StockLedgerDivisionValueHelp',
			Label: 'Stock Ledger Division',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterOut', LocalDataProperty: 'stockLedgerDivision', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
	stockLedgerSubdivision
		@Common.Label: 'Stock Ledger Subdivision'
		@sap.value.list: 'fixed-values'
		@Common.Text: {
			"$value": "stockLedgerSubdivisionValueHelp/description", 
			"@UI.TextArrangement": #TextFirst
		}		
		@Common.ValueList: {
			CollectionPath: 'StockLedgerSubdivisionValueHelp',
			Label: 'Stock Ledger Subdivision',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterOut', LocalDataProperty: 'stockLedgerSubdivision', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
	movementEntryBehavior
		@Common.Label: 'Movement Entry Behaviour'
		@sap.value.list: 'fixed-values'
		@Common.Text: {
			"$value": "movementEntryBehaviorValueHelp/description",
			"@UI.TextArrangement": #TextFirst
		}
		@Common.ValueList: {
			CollectionPath: 'MovementEntryBehaviorValueHelp',
			Label: 'Movement Entry Behaviour',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterOut', LocalDataProperty: 'movementEntryBehavior', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
};

// List Report columns

annotate StockLedgerCharacteristicsService.StockLedgerGroup with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: id},
		{$Type: 'UI.DataField', Value: description},
		{$Type: 'UI.DataField', Value: stockLedgerDivision},
		{$Type: 'UI.DataField', Value: stockLedgerSubdivision},
		{$Type: 'UI.DataField', Value: movementEntryBehavior}
	],
	
// List report filters
	
	UI.SelectionFields: [ id ],
	
// Object page header
	
	UI.HeaderInfo: {
		Title: { Value: description },
		TypeName:'Stock Ledger Group', 
        TypeNamePlural:'Stock Ledger Groups'
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
			Label:'Stock Ledger Group Details', //anchor title		
		}
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: id}
	],

// Object page field groups
	
	UI.FieldGroup#Values: {
		Data: [
			{$Type: 'UI.DataField', Value: description},
			{$Type: 'UI.DataField', Value: stockLedgerDivision},
			{$Type: 'UI.DataField', Value: stockLedgerSubdivision},
			{$Type: 'UI.DataField', Value: movementEntryBehavior}
		]
	}

);