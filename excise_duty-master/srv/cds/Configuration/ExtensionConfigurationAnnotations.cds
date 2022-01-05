using ExtensionConfigurationService from './ExtensionConfigurationService'; 

annotate ExtensionConfigurationService.ExtensionFunctionConfig with {

	exciseDutyTypeId
		@Common.Label: 'Excise Duty Type'
		@Common.FieldControl: #ReadOnly
		@sap.value.list: 'fixed-values'
		@Common.ValueList: { 
			CollectionPath: 'ExciseDutyType',
			Label: 'Excise Duty Type',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'exciseDutyTypeId', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
		
	extensionPoint
		@Common.Label: 'Extension Point'
		@Common.FieldControl: #ReadOnly
		@sap.value.list: 'fixed-values'
		@Common.ValueList: { 
			CollectionPath: 'ExtensionPoint',
			Label: 'Extension Point',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'extensionPoint', ValueListProperty: 'extensionPoint'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'},
			]
		};
		
	extensionDestination
		@Common.Label: 'Destination Name'
		@Common.Text: 'Name of the destination service destination';
};

// List Report columns

annotate ExtensionConfigurationService.ExtensionFunctionConfig with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: exciseDutyTypeId},
		{$Type: 'UI.DataField', Value: extensionPoint},
		{$Type: 'UI.DataField', Value: extensionDestination}
	],
	
// List report filters
	
	UI.SelectionFields: [ exciseDutyTypeId ],
	
// Object page header
	
	UI.HeaderInfo: {
		Title: { Value: description },
		TypeName:'Extension Function Configuration', 
        TypeNamePlural:'Extension Function Configurations'
	},

// Object page facets

	UI.Facets:
	[
		{
			$Type:'UI.CollectionFacet', 
			Facets: [
						{ $Type:'UI.ReferenceFacet', Label: 'Assignment', Target: '@UI.Identification' },
						{ $Type:'UI.ReferenceFacet', Label: 'Destination', Target: '@UI.FieldGroup#Values' }
					],
			Label:'Extension Function Configuration Details', //anchor title		
		},
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: exciseDutyTypeId},
		{$Type: 'UI.DataField', Value: extensionPoint}
	],

// Object page field groups
	
	UI.FieldGroup#Values: {
		Data: [
			{$Type: 'UI.DataField', Value: extensionDestination}
		]
	}
);