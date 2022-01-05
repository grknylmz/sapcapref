using MaterialMasterService from './MaterialMasterService'; 

annotate MaterialMasterService.ExciseDutyType with {
	id
		@Common.Label: 'ID'
		@Common.FieldControl: #ReadOnly
		@Common.Text: "description";
	description
		@Common.Label: 'Description';
};

annotate MaterialMasterService.MaterialDescription with {
	description
		@Common.Label: 'Description';
}

annotate MaterialMasterService.MaterialMasterExtension with {
	materialNumber
		@Common.Label: 'Material Number'
		@Common.ValueList: { 
			CollectionPath: 'A_Product',
			Label: 'Product',
			SearchSupported: 'true',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'materialNumber', ValueListProperty: 'Product'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'Product'}
			]
		};
	companyCode
		@Common.Label: 'Company Code';
	exciseDutyTypeId
		@Common.Label: 'Excise Duty Type'
		@Common.FieldControl: #ReadOnly
		@sap.value.list: 'fixed-values'
		@Common.Text: {
			"$value": "exciseDutyType/description",
			"@UI.TextArrangement": #TextFirst
		}
		@Common.ValueList: { 
			CollectionPath: 'ExciseDutyType',
			Label: 'Excise Duty Type',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'exciseDutyTypeId', ValueListProperty: 'id'}
			]
		};
	exciseDutyProcurementIndicator
		@Common.Label: 'Excise Duty Procurement Indicator';
	taxWarehouseRegistrationOfManufacturingPlant
		@Common.Label: 'Tax Warehouse Registration of Manufacturing Plant';
	exciseDutyNumberForTaxWarehouse
		@Common.Label: 'Excise Duty Number for Tax Warehouse';
	exciseDutyTypeIndependentMaterialGroup
		@Common.Label: 'Excise Duty Type Independent Material Group';
	alcoholicStrength
		@Common.Label: 'Alcoholic Strength (% Vol., Degree Plato)';
	validFrom
		@Common.Label : 'Valid From';
};

// List Report columns

annotate MaterialMasterService.MaterialMasterExtension with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: materialNumber},
		{$Type: 'UI.DataField', Value: "materialDescription/description"},
		{$Type: 'UI.DataField', Value: companyCode},
		{$Type: 'UI.DataField', Value: exciseDutyTypeId},
		{$Type: 'UI.DataField', Value: exciseDutyProcurementIndicator},
		{$Type: 'UI.DataField', Value: taxWarehouseRegistrationOfManufacturingPlant},
		{$Type: 'UI.DataField', Value: exciseDutyNumberForTaxWarehouse},
		{$Type: 'UI.DataField', Value: exciseDutyTypeIndependentMaterialGroup},		
		{$Type: 'UI.DataField', Value: alcoholicStrength},
		{$Type: 'UI.DataField', Value: validFrom},
	],
	
// List report filters
	
	UI.SelectionFields: [ exciseDutyTypeId ],
	
// Object page header
	
	UI.HeaderInfo: {
		Title: { Value: description },
		TypeName:'Material Master Extension', 
        TypeNamePlural:'Material Master Extensions'
	},

// Object page facets

	UI.Facets:
	[
		{
			$Type:'UI.CollectionFacet', 
			Facets: [
						{ $Type:'UI.ReferenceFacet', Label: 'General Info', Target: '@UI.Identification' },
						{ $Type:'UI.ReferenceFacet', Label: 'Values', Target: '@UI.FieldGroup#Values' },
						{ $Type:'UI.ReferenceFacet', Label: 'Procurement', Target: '@UI.FieldGroup#Procurement' },
					],
			Label:'Material Master Extension Details', //anchor title		
		},
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: materialNumber},
		{$Type: 'UI.DataField', Value: companyCode},
		{$Type: 'UI.DataField', Value: exciseDutyTypeId}
	],

// Object page field groups
	
	UI.FieldGroup#Values: {
		Data: [
			{$Type: 'UI.DataField', Value: exciseDutyTypeIndependentMaterialGroup},
			{$Type: 'UI.DataField', Value: alcoholicStrength},
			{$Type: 'UI.DataField', Value: validFrom},
		]
	},
	UI.FieldGroup#Procurement: {
		Data: [
			{$Type: 'UI.DataField', Value: exciseDutyProcurementIndicator},
			{$Type: 'UI.DataField', Value: taxWarehouseRegistrationOfManufacturingPlant},
			{$Type: 'UI.DataField', Value: exciseDutyNumberForTaxWarehouse}
		]
	}
);