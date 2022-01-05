using StockLedgerService from './StockLedgerService'; 

annotate StockLedgerService.StockLedgerLineItem with {
	materialDocumentYear
		@sap.label: 'Material Document Year'
		@Common.Label: 'Material Document Year'
		@sap.aggregation.role: 'dimension'
		@Common.FieldControl: #ReadOnly;
	materialDocumentNumber
		@sap.label: 'Material Document Number'
		@Common.Label: 'Material Document Number'
		@sap.aggregation.role: 'dimension'
		@Common.FieldControl: #ReadOnly;
	materialDocumentItem
		@sap.label: 'Document Item'
		@Common.Label: 'Document Item'
		@sap.aggregation.role: 'dimension'
		@Common.FieldControl: #ReadOnly;
	exciseDutyPositionNumber
		@sap.label: 'Duty Position Number'
		@Common.Label: 'Duty Position Number'
		@sap.aggregation.role: 'dimension'
		@Common.FieldControl: #ReadOnly;
	exciseDutyTypeId
		@sap.label: 'Excise Duty Type'
		@Common.Label: 'Excise Duty Type'
		@sap.aggregation.role: 'dimension';
	companyCode
		@sap.label: 'Company Code'
		@Common.Label: 'Company Code'
		@sap.aggregation.role: 'dimension';
	taxWarehouseRegistration
		@sap.label: 'Tax Warehouse Registration'
		@Common.Label: 'Tax Warehouse Registration'
		@sap.aggregation.role: 'dimension';
	plant
		@sap.label: 'Plant'
		@Common.Label: 'Plant'
		@sap.aggregation.role: 'dimension';
	storageLocation
		@sap.label: 'Storage Location'
		@Common.Label: 'Storage Location'
		@sap.aggregation.role: 'dimension';
	materialDocumentPostingDate
		@sap.label: 'Material Document Posting Date'
		@Common.Label: 'Material Document Posting Date'
		@sap.aggregation.role: 'dimension';
	stockLedgerNumber
		@sap.label: 'Stock Ledger Number'
		@Common.Label: 'Stock Ledger Number'
		@sap.aggregation.role: 'dimension';
	noEntryInExtendedStockLedger
		@sap.label: 'No Entry in Extended Stock Ledger'
		@Common.Label: 'No Entry in Extended Stock Ledger'
		@sap.aggregation.role: 'dimension';
	materialNumber
		@sap.label: 'Material Number'
		@Common.Label: 'Material Number'
		@sap.aggregation.role: 'dimension';
	batchNumber
		@sap.label: 'Batch Number'
		@Common.Label: 'Batch Number'
		@sap.aggregation.role: 'dimension';
	quantity
		@sap.label: 'Quantity'
		@Common.Label: 'Quantity'
		@sap.aggregation.role: 'dimension';
	baseUnitOfMeasure
		@sap.label: 'Unit of Measure'
		@Common.Label: 'Unit of Measure'
		@sap.aggregation.role: 'dimension';
	edMovementCategoryId
		@sap.label: 'Movement Category'
		@Common.Label: 'Movement Category'
		@sap.aggregation.role: 'dimension';
	erpMovementType
		@sap.label: 'Movement Type'
		@Common.Label: 'Movement Type'
		@sap.aggregation.role: 'dimension';
	stockLedgerGroupId
		@sap.label: 'Stock Ledger Group'
		@Common.Label: 'Stock Ledger Group'
		@sap.aggregation.role: 'dimension';
	stockLedgerDivision
		@sap.label: 'Stock Ledger Division'
		@Common.Label: 'Stock Ledger Division'
		@sap.aggregation.role: 'dimension';
	stockLedgerSubdivision
		@sap.label: 'Stock Ledger Subdivision'
		@Common.Label: 'Stock Ledger Subdivision'
		@sap.aggregation.role: 'dimension';
	alcoholicStrength
		@sap.label: '% Vol.'
		@Common.Label: '% Vol.'
		@sap.aggregation.role: 'dimension';
	taxWarehouseRegistrationOfManufacturingPlant
		@sap.label: 'Tax Warehouse Registration of Manufacturing Plant'
		@Common.Label: 'Tax Warehouse Registration of Manufacturing Plant'
		@sap.aggregation.role: 'dimension';
	exciseDutyNumberForTaxWarehouse
		@sap.label: 'Excise Duty Number for Tax Warehouse'
		@Common.Label: 'Excise Duty Number for Tax Warehouse'
		@sap.aggregation.role: 'dimension';
	externalTaxWarehouseRegistration
		@sap.label: 'External Tax Warehouse Registration'
		@Common.Label: 'External Tax Warehouse Registration'
		@sap.aggregation.role: 'dimension';
	externalExciseDutyNumber
		@sap.label: 'External Excise Duty Number'
		@Common.Label: 'External Excise Duty Number'
		@sap.aggregation.role: 'dimension';
	taxValueAmount
		@sap.label: 'Tax Value Amount'
		@Common.Label: 'Tax Value Amount'
		@sap.aggregation.role: 'dimension';
	taxValueCurrency
		@sap.label: 'Tax Value Currency'
		@Common.Label: 'Tax Value Currency'
		@sap.aggregation.role: 'dimension';
	salesOrderNumber
		@sap.label: 'Sales Order Number'
		@Common.Label: 'Sales Order Number'
		@sap.aggregation.role: 'dimension';
	salesOrderItem
		@sap.label: 'Sales Order Item'
		@Common.Label: 'Sales Order Item'
		@sap.aggregation.role: 'dimension';
};

// List Report columns

annotate StockLedgerService.StockLedgerLineItem with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataField', Value: materialDocumentYear},
		{$Type: 'UI.DataField', Value: materialDocumentNumber},
		{$Type: 'UI.DataField', Value: materialDocumentItem},
		{$Type: 'UI.DataField', Value: exciseDutyTypeId},
		{$Type: 'UI.DataField', Value: companyCode},
		{$Type: 'UI.DataField', Value: taxWarehouseRegistration},
		{$Type: 'UI.DataField', Value: plant},
		{$Type: 'UI.DataField', Value: storageLocation},
		{$Type: 'UI.DataField', Value: materialDocumentPostingDate},
		{$Type: 'UI.DataField', Value: stockLedgerNumber},
		{$Type: 'UI.DataField', Value: noEntryInExtendedStockLedger},
		{$Type: 'UI.DataField', Value: materialNumber},
		{$Type: 'UI.DataField', Value: batchNumber},
		{$Type: 'UI.DataField', Value: quantity},
		{$Type: 'UI.DataField', Value: baseUnitOfMeasure},
		{$Type: 'UI.DataField', Value: edMovementCategoryId},
		{$Type: 'UI.DataField', Value: erpMovementType},
		{$Type: 'UI.DataField', Value: stockLedgerGroupId},
		{$Type: 'UI.DataField', Value: stockLedgerDivision},
		{$Type: 'UI.DataField', Value: stockLedgerSubdivision},
		{$Type: 'UI.DataField', Value: alcoholicStrength},
		{$Type: 'UI.DataField', Value: taxWarehouseRegistrationOfManufacturingPlant},
		{$Type: 'UI.DataField', Value: exciseDutyNumberForTaxWarehouse},
		{$Type: 'UI.DataField', Value: externalTaxWarehouseRegistration},
		{$Type: 'UI.DataField', Value: externalExciseDutyNumber},
		{$Type: 'UI.DataField', Value: taxValueAmount},
		{$Type: 'UI.DataField', Value: taxValueCurrency},
		{$Type: 'UI.DataField', Value: salesOrderNumber},
		{$Type: 'UI.DataField', Value: salesOrderItem},
		{$Type: 'UI.DataField', Value: exciseDutyPositionNumber},
		{$Type: 'UI.DataField', Value: customerNumber},
	],
	
// List report filters
	
	UI.SelectionFields: [ materialDocumentPostingDate, taxWarehouseRegistration, stockLedgerNumber, stockLedgerDivision ],
	
// Object page header
	
	UI.HeaderInfo: {
		Title: { Value: description },
		TypeName:'Stock Ledger Line Item', 
        TypeNamePlural:'Stock Ledger Line Items'
	},

// Object page facets

	UI.Facets:
	[
		{
			$Type:'UI.CollectionFacet', 
			Facets: [
						{ $Type:'UI.ReferenceFacet', Label: 'General Info', Target: '@UI.Identification' },
						{ $Type:'UI.ReferenceFacet', Label: 'Values 1', Target: '@UI.FieldGroup#Values1' },
						{ $Type:'UI.ReferenceFacet', Label: 'Values 2', Target: '@UI.FieldGroup#Values2' }
					],
			Label:'Stock Ledger Line Item Details', //anchor title		
		},
	],	

	UI.Identification:
	[
		{$Type: 'UI.DataField', Value: materialDocumentYear},
		{$Type: 'UI.DataField', Value: materialDocumentNumber},
		{$Type: 'UI.DataField', Value: materialDocumentItem},
		{$Type: 'UI.DataField', Value: exciseDutyPositionNumber},
		{$Type: 'UI.DataField', Value: exciseDutyTypeId}
	],

// Object page field groups
	
	UI.FieldGroup#Values1: {
		Data: [
			{$Type: 'UI.DataField', Value: companyCode},
			{$Type: 'UI.DataField', Value: taxWarehouseRegistration},
			{$Type: 'UI.DataField', Value: plant},
			{$Type: 'UI.DataField', Value: storageLocation},
			{$Type: 'UI.DataField', Value: materialDocumentPostingDate},
			{$Type: 'UI.DataField', Value: stockLedgerNumber},
			{$Type: 'UI.DataField', Value: noEntryInExtendedStockLedger},
			{$Type: 'UI.DataField', Value: materialNumber},
			{$Type: 'UI.DataField', Value: batchNumber},
			{$Type: 'UI.DataField', Value: quantity},
			{$Type: 'UI.DataField', Value: baseUnitOfMeasure},
			{$Type: 'UI.DataField', Value: edMovementCategoryId},
			{$Type: 'UI.DataField', Value: customerNumber},
		]
	},
	
	UI.FieldGroup#Values2: {
		Data: [
			{$Type: 'UI.DataField', Value: erpMovementType},
			{$Type: 'UI.DataField', Value: stockLedgerGroupId},
			{$Type: 'UI.DataField', Value: stockLedgerDivision},
			{$Type: 'UI.DataField', Value: stockLedgerSubdivision},
			{$Type: 'UI.DataField', Value: alcoholicStrength},
			{$Type: 'UI.DataField', Value: taxWarehouseRegistrationOfManufacturingPlant},
			{$Type: 'UI.DataField', Value: exciseDutyNumberForTaxWarehouse},
			{$Type: 'UI.DataField', Value: externalTaxWarehouseRegistration},
			{$Type: 'UI.DataField', Value: externalExciseDutyNumber},
			{$Type: 'UI.DataField', Value: salesOrderNumber},
			{$Type: 'UI.DataField', Value: salesOrderItem}
		]
	}

);