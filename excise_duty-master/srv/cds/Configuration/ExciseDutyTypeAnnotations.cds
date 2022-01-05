using ExciseDutyTypeService from './ExciseDutyTypeService'; 

annotate ExciseDutyTypeService.ContainerContentCalculationIndicatorValueHelp with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly
		@Common.Text: "description";
	description
		@Common.Label: 'Description';
};

annotate ExciseDutyTypeService.ExciseDutyType with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly;
	description
		@Common.Label: 'Description';
	containerContentCalculationIndicator
		@Common.Label: 'Calculation Indicator'
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
				{ $Type: 'Common.ValueListParameterOut', LocalDataProperty: 'ContainerContentCalculationIndicatorValueHelp', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
	containerContentUnit
		@Common.Label: 'Unit';
	containerContentDecimalPlaces
		@Common.Label: 'Decimals';
};

// List Report columns

annotate ExciseDutyTypeService.ExciseDutyType with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: id},
		{$Type: 'UI.DataField', Value: description},
		{$Type: 'UI.DataField', Value: containerContentCalculationIndicator},
		{$Type: 'UI.DataField', Value: containerContentUnit},
		{$Type: 'UI.DataField', Value: containerContentDecimalPlaces},		
	],
	
// List report filters
	
	UI.SelectionFields: [ id ],
	
// Object page header
	
	UI.HeaderInfo: {
		Title: { Value: description },
		TypeName:'Excise Duty Type', 
        TypeNamePlural:'Excise Duty Types'
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
			Label:'Excise Duty Type Details', //anchor title		
		},
		{
			$Type:'UI.ReferenceFacet',
			Label: 'Settlement Units',
			Target: 'settlementUnits/@UI.LineItem'
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
			{$Type: 'UI.DataField', Value: containerContentCalculationIndicator},
			{$Type: 'UI.DataField', Value: containerContentUnit},
			{$Type: 'UI.DataField', Value: containerContentDecimalPlaces},
		]
	}

);

annotate ExciseDutyTypeService.SettlementUnit with {
        companyCode
        	@Common.Label: 'Company Code'
        	@Common.FieldControl: #ReadOnly;
        exciseDutyTypeId
        	@Common.Label: 'Excise Duty Type'
        	@Common.FieldControl: #ReadOnly;
        settlementUnit
        	@Common.Label: 'Settlement Unit';
        baseQuantityUnit
        	@Common.Label: 'Base Quantity Unit';
        volumeDecimalPlaces
        	@Common.Label: 'Volume Decimal Places';
};

annotate ExciseDutyTypeService.SettlementUnit with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: companyCode},
		{$Type: 'UI.DataField', Value: settlementUnit},
		{$Type: 'UI.DataField', Value: baseQuantityUnit},
		{$Type: 'UI.DataField', Value: volumeDecimalPlaces}
	],

// Object page header

	UI.HeaderInfo: {
		Title: { Value: settlementUnit },
		TypeName:'Settlement Unit', 
        TypeNamePlural:'Settlement Units'
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
			Label:'Settlement Unit Details', //anchor title		
		},
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: companyCode}
	],

// Object page field groups
	
	UI.FieldGroup#Values: {
		Data: [
			{$Type: 'UI.DataField', Value: settlementUnit},
			{$Type: 'UI.DataField', Value: baseQuantityUnit},
			{$Type: 'UI.DataField', Value: volumeDecimalPlaces}
		]
	}
	
);