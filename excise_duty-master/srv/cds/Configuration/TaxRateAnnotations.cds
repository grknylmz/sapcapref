using TaxRateService from './TaxRateService'; 

annotate TaxRateService.ExciseDutyType with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly
		@Common.Text: "description";
	description
		@Common.Label: 'Description';
};

annotate TaxRateService.TaxRate with {
	country
		@Common.Label : 'Country'
		@Common.FieldControl: #ReadOnly;
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
				{ $Type: 'Common.ValueListParameterOut', LocalDataProperty: 'exciseDutyTypeId', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
    baseQuantityUnit
	    @Common.FieldControl: #ReadOnly
		@Common.Label : 'Base Quantitiy Unit';
    validFrom
		@Common.FieldControl: #ReadOnly
    	@Common.Label : 'Valid From';
    alcoholicStrengthLowerLimit
		@Common.FieldControl: #ReadOnly
    	@Common.Label: '% Vol. Lower Limit';
    taxRate
    	@Common.Label : 'Tax Rate';
    currency
    	@Common.Label : 'Currency';
};

// List Report columns

annotate TaxRateService.TaxRate with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: country},
		{$Type: 'UI.DataField', Value: exciseDutyTypeId},
		{$Type: 'UI.DataField', Value: baseQuantityUnit},
		{$Type: 'UI.DataField', Value: validFrom},
		{$Type: 'UI.DataField', Value: alcoholicStrengthLowerLimit},
		{$Type: 'UI.DataField', Value: taxRate},
		{$Type: 'UI.DataField', Value: currency},		
	],
	
// List report filters
	
	UI.SelectionFields: [ exciseDutyTypeId ],
	
// Object page header
	
	UI.HeaderInfo: {
		Title: { Value: description },
		TypeName:'Tax Rate', 
        TypeNamePlural:'Tax Rates'
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
			Label:'Tax Rate Details', //anchor title		
		},
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: country},
		{$Type: 'UI.DataField', Value: exciseDutyTypeId},
		{$Type: 'UI.DataField', Value: baseQuantityUnit},
		{$Type: 'UI.DataField', Value: validFrom},
		{$Type: 'UI.DataField', Value: alcoholicStrengthLowerLimit}
	],

// Object page field groups
	
	UI.FieldGroup#Values: {
		Data: [
			{$Type: 'UI.DataField', Value: taxRate},
			{$Type: 'UI.DataField', Value: currency},
		]
	}
);