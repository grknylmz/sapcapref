using TaxWarehouseService from './TaxWarehouseService'; 

annotate TaxWarehouseService.ExciseDutyType with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly
		@Common.Text: "description";
	description
		@Common.Label: 'Description';
};

annotate TaxWarehouseService.TaxWarehouse with {
	exciseDutyTypeId 
		@Common.Label: 'Excise Duty Type'
		@Common.FieldControl: #ReadOnly
		@sap.value.list: 'fixed-values'
//		@Common.Text: {
//			"$value": "description", //seems to be not needed but can't delete for the generation
//			"@UI.TextArrangement": #TextFirst
//		}
		@Common.ValueList: { 
			CollectionPath: 'ExciseDutyType',
			Label: 'Excise Duty Type',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'exciseDutyTypeId', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
	validTo 
		@Common.Label : 'Valid To';
	validFrom 
		@Common.Label : 'Valid From';
	companyCode 
		@Common.Label : 'Company Code';
	taxWarehouseRegistration 
		@Common.Label : 'Tax Warehouse Registration'
		@Common.FieldControl: #ReadOnly;
	useStockLedgerSubdivisions 
		@Common.Label : 'Stock Ledger Subdivisions';
	stockLedgerType 
		@Common.Label : 'Stock Ledger Type';

};

annotate TaxWarehouseService.TaxWarehouse with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: exciseDutyTypeId},
		{$Type: 'UI.DataField', Value: taxWarehouseRegistration},
		{$Type: 'UI.DataField', Value: validFrom},
		{$Type: 'UI.DataField', Value: validTo},
		{$Type: 'UI.DataField', Value: companyCode},
		{$Type: 'UI.DataField', Value: useStockLedgerSubdivisions},
		{$Type: 'UI.DataField', Value: stockLedgerType},
		
	],
	UI.SelectionFields: [ exciseDutyTypeId, companyCode, validFrom ],
	UI.HeaderInfo: {
		Title: { Value: taxWarehouseRegistration },
		TypeName:'Tax Warehouse', 
        TypeNamePlural:'Tax Warehouses'
	},

	UI.DataPoint: {
		Title:'General Info',
        Value:taxWarehouseRegistration,
        Description: 'Tax Warehouse Registration',
		Visualization: #Number
	},
	UI.FieldGroup#Values: {
		Data: [
			{$Type: 'UI.DataField', Value: validFrom},
			{$Type: 'UI.DataField', Value: validTo},
			{$Type: 'UI.DataField', Value: companyCode},
			{$Type: 'UI.DataField', Value: useStockLedgerSubdivisions},
			{$Type: 'UI.DataField', Value: stockLedgerType}
		]
	},

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: exciseDutyTypeId},
		{$Type: 'UI.DataField', Value: taxWarehouseRegistration, Label : 'Tax Warehouse Registration'},
		{$Type: 'UI.DataField', Value: storageLocation}
	],

	UI.Facets:
	[
		{
			$Type:'UI.CollectionFacet', 
			Facets: [
						{ $Type:'UI.ReferenceFacet', Label: 'General Info', Target: '@UI.Identification' },
						{ $Type:'UI.ReferenceFacet', Label: 'Values', Target: '@UI.FieldGroup#Values' }
					],
			Label:'Tax Warehouse Details',		
		},
		{$Type:'UI.ReferenceFacet', Label: 'Tax Warehouse Assignments', Target: 'taxWarehouseAssignments/@UI.LineItem'},
	]	

);

annotate TaxWarehouseService.TaxWarehouseAssignment with {
	plant 
		@Common.Label: 'Plant'
		@Common.FieldControl: #ReadOnly;
	storageLocation 
		@Common.Label: 'Storage Location'
		@Common.FieldControl: #ReadOnly;
	validFrom 
		@Common.Label: 'Valid From'
		@Common.FieldControl: #ReadOnly;
	validTo 
		@Common.Label: 'Valid To';
	taxWarehouseRegistration 
		@Common.Label: 'Tax Warehouse Registration'
		@Common.FieldControl: #ReadOnly;
	exciseDutyTypeId 
		@Common.Label: 'Excise Duty Type'
		@Common.FieldControl: #ReadOnly;

};

annotate TaxWarehouseService.TaxWarehouseAssignment with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: plant},
		{$Type: 'UI.DataField', Value: storageLocation},
		{$Type: 'UI.DataField', Value: validFrom},
		{$Type: 'UI.DataField', Value: validTo}
	],

	UI.HeaderInfo: {
		Title: { Value: storageLocation },
		TypeName:'Tax Warehouse', 
        TypeNamePlural:'Tax Warehouses'
	},

	UI.FieldGroup: {
		Data: [
			{$Type: 'UI.DataField', Value: validTo}
		]
	},

	UI.Identification:
	[
			{$Type: 'UI.DataField', Value: plant},
			{$Type: 'UI.DataField', Value: storageLocation},
			{$Type: 'UI.DataField', Value: validFrom}

	],
	UI.Facets:
	[
		{
			$Type:'UI.CollectionFacet', 
			Facets: [
						{ $Type:'UI.ReferenceFacet', Label: 'Assignment', Target: '@UI.Identification' },
						{ $Type:'UI.ReferenceFacet', Label: 'Validity', Target: '@UI.FieldGroup' }
					],
			Label:'Tax Warehouse Assignment',		
		}
	]	
);


