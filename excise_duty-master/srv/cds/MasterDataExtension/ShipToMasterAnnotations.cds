using ShipToMasterService from './ShipToMasterService'; 

//value help labels

annotate ShipToMasterService.SpecialPartnerType with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly
		@Common.Text: "description";
	description
		@Common.Label: 'Description';
};

annotate ShipToMasterService.ShipToMasterExtension with {
	
	customerNumber
		@Common.Label: 'Customer Number'
		@Common.ValueList: { 
			CollectionPath: 'A_Customer',
			Label: 'Customer',
			SearchSupported: 'true',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'customerNumber', ValueListProperty: 'Customer'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'CustomerName'},
			]
		};
	validFrom
		@Common.Label: 'Valid From';
	validTo
		@Common.Label: 'Valid to';
	externalTaxWarehouseRegistration
		@Common.Label: 'Tax Warehouse Registration';
	externalExciseDutyNumber
		@Common.Label: 'Excise Duty Number';
	exciseDutySpecialPartnerTypeId
		@Common.Label: 'Special Partner Type'
		@sap.value.list: 'fixed-values'
//		@Common.Text: {
//			"$value": "description", //seems to be not needed but can't delete for the generation
//			"@UI.TextArrangement": #TextFirst
//		}
		@Common.ValueList: { 
			CollectionPath: 'SpecialPartnerType',
			Label: 'Special Partner Type',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'exciseDutySpecialPartnerTypeId', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
	thirdCountryIndicator
		@Common.Label: 'Third Country Indicator';
};

annotate ShipToMasterService.Customer with {
	name
		@Common.Label: 'Name';
	countryKey
		@Common.Label: 'Country';
};

annotate ShipToMasterService.ShipToMasterExtension with @(

// List Report columns
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: customerNumber},
		{$Type: 'UI.DataField', Value: "customerData/name"},
		{$Type: 'UI.DataField', Value: "customerData/countryKey"},
		{$Type: 'UI.DataField', Value: validFrom},
		{$Type: 'UI.DataField', Value: validTo},
		{$Type: 'UI.DataField', Value: externalTaxWarehouseRegistration},
		{$Type: 'UI.DataField', Value: externalExciseDutyNumber},
		{$Type: 'UI.DataField', Value: exciseDutySpecialPartnerTypeId},
		{$Type: 'UI.DataField', Value: thirdCountryIndicator}
	],

// Object page header

	UI.HeaderInfo: {
		TypeName:'Ship To Master Extension', 
        TypeNamePlural:'Ship To Master Extensions'
	},
	
	// Object page facets

	UI.Facets:
	[
		{
			$Type:'UI.CollectionFacet', 
			Facets: [
						{ $Type:'UI.ReferenceFacet', Label: 'General Info', Target: '@UI.Identification' },
						{ $Type:'UI.ReferenceFacet', Label: 'Values', Target: '@UI.FieldGroup#Values' },
						{ $Type:'UI.ReferenceFacet', Label: 'External Excise Duty Data', Target: '@UI.FieldGroup#ExternalData' }
					],
			Label:'Ship To Master Extension Details', //anchor title		
		},
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: customerNumber},
		{$Type: 'UI.DataField', Value: validFrom}
	],

// Object page field groups
	
	UI.FieldGroup#Values: {
		Data: [
			{$Type: 'UI.DataField', Value: validTo},
			{$Type: 'UI.DataField', Value: exciseDutySpecialPartnerTypeId},
			{$Type: 'UI.DataField', Value: thirdCountryIndicator}
		]
	},
	
	UI.FieldGroup#ExternalData: {
		Data: [
			{$Type: 'UI.DataField', Value: externalTaxWarehouseRegistration},
			{$Type: 'UI.DataField', Value: externalExciseDutyNumber}
		]
	}
	
);