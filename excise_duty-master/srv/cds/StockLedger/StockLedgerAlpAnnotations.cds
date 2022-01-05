using StockLedgerAlpService from './StockLedgerAlpService'; 

annotate StockLedgerAlpService.StockLedgerDivisionValueHelp with {
	id @(
		Common.Label: 'ID',
		Common.FieldControl: #ReadOnly,
		Common.Text: "description"
	);
	description @(
		Common.Label: 'Description'
	);
};

annotate StockLedgerAlpService.StockLedgerLineItem with {
	materialDocumentYear @(
		sap.label: 'Material Document Year',
		Common.Label: 'Material Document Year',
		Common.FieldControl: #ReadOnly,
		Common.ValueList#VisualFilter: { 
			CollectionPath: 'StockLedgerLineItem',
			Label: 'Material Document Posting Date',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'stockLedgerDivision', ValueListProperty: 'stockLedgerDivision'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialDocumentPostingDate', ValueListProperty: 'materialDocumentPostingDate'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'taxWarehouseRegistration', ValueListProperty: 'taxWarehouseRegistration'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialNumber', ValueListProperty: 'materialNumber'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'stockLedgerNumber', ValueListProperty: 'stockLedgerNumber'},
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'materialDocumentYear', ValueListProperty: 'materialDocumentYear'}
			],
			PresentationVariantQualifier: 'TVAmount'
		}
	);
	materialDocumentNumber @(
		sap.label: 'Material Document Number',
		Common.Label: 'Material Document Number',
		Common.FieldControl: #ReadOnly
	);
	materialDocumentItem @(
		sap.label: 'Document Item',
		Common.Label: 'Document Item',
		Common.FieldControl: #ReadOnly
	);
	exciseDutyPositionNumber @(
		sap.label: 'Duty Position Number',
		Common.Label: 'Duty Position Number',
		Common.FieldControl: #ReadOnly
	);
	exciseDutyTypeId @(
		sap.label: 'Excise Duty Type',
		Common.Label: 'Excise Duty Type'
	);
	companyCode @(
		sap.label: 'Company Code',
		Common.Label: 'Company Code'
	);
	taxWarehouseRegistration @(
		sap.label: 'Tax Warehouse Registration',
		Common.Label: 'Tax Warehouse Registration',
		sap.value.list: 'fixed-values',
		Common.ValueList: { 
			CollectionPath: 'TaxWarehouse',
			Label: 'Tax Warehouse Registration',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'taxWarehouseRegistration', ValueListProperty: 'taxWarehouseRegistration'}
			]
		},
		Common.ValueList#VisualFilter: { 
			CollectionPath: 'StockLedgerLineItem',
			Label: 'Tax Warehouse Registration',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'stockLedgerDivision', ValueListProperty: 'stockLedgerDivision'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialDocumentYear', ValueListProperty: 'materialDocumentYear'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialDocumentPostingDate', ValueListProperty: 'materialDocumentPostingDate'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialNumber', ValueListProperty: 'materialNumber'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'stockLedgerNumber', ValueListProperty: 'stockLedgerNumber'},
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'taxWarehouseRegistration', ValueListProperty: 'taxWarehouseRegistration'}
			],
			PresentationVariantQualifier: 'TWHRegistration'
		}
	);
	plant @(
		sap.label: 'Plant',
		Common.Label: 'Plant'
	);
	storageLocation @(
		sap.label: 'Storage Location',
		Common.Label: 'Storage Location'
	);
	materialDocumentPostingDate @(
		sap.label: 'Material Document Posting Date',
		Common.Label: 'Material Document Posting Date',
		Common.ValueList: { 
			CollectionPath: 'StockLedgerLineItem',
			Label: 'Material Document Posting Date',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'materialDocumentPostingDate', ValueListProperty: 'materialDocumentPostingDate'}
			]
		}
	);
	stockLedgerNumber @(
		sap.label: 'Stock Ledger Number',
		Common.Label: 'Stock Ledger Number'
	);
	noEntryInExtendedStockLedger @(
		sap.label: 'No Entry in Extended Stock Ledger',
		Common.Label: 'No Entry in Extended Stock Ledger'
	);
	materialNumber @(
		sap.label: 'Material Number',
		Common.Label: 'Material Number',
		Common.ValueList#VisualFilter: { 
			CollectionPath: 'StockLedgerLineItem',
			Label: 'Material Number',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'stockLedgerDivision', ValueListProperty: 'stockLedgerDivision'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialDocumentYear', ValueListProperty: 'materialDocumentYear'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialDocumentPostingDate', ValueListProperty: 'materialDocumentPostingDate'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'taxWarehouseRegistration', ValueListProperty: 'taxWarehouseRegistration'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'stockLedgerNumber', ValueListProperty: 'stockLedgerNumber'},
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'materialNumber', ValueListProperty: 'materialNumber'}
			],
			PresentationVariantQualifier: 'MNumber'
		},
	);
	batchNumber @(
		sap.label: 'Batch Number',
		Common.Label: 'Batch Number'
	);
	quantity @(
		sap.label: 'Quantity',
		Common.Label: 'Quantity'
	);
	baseUnitOfMeasure @(
		sap.label: 'Unit of Measure',
		Common.Label: 'Unit of Measure'
	);
	edMovementCategoryId @(
		sap.label: 'Movement Category',
		Common.Label: 'Movement Category'
	);
	erpMovementType @(
		sap.label: 'Movement Type',
		Common.Label: 'Movement Type'
	);
	stockLedgerGroupId @(
		sap.label: 'Stock Ledger Group',
		Common.Label: 'Stock Ledger Group'
	);
	stockLedgerDivision @(
		sap.label: 'Stock Ledger Division',
		Common.Label: 'Stock Ledger Division',
		sap.value.list: 'fixed-values',
//		Common.Text: {
//				"$value": "description", //seems to be not needed but can't delete for the generation
//				"@UI.TextArrangement": #TextFirst
//			},
		Common.ValueList#VisualFilter: { 
			CollectionPath: 'StockLedgerLineItem',
			Label: 'Stock Ledger Division',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialDocumentYear', ValueListProperty: 'materialDocumentYear'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialDocumentPostingDate', ValueListProperty: 'materialDocumentPostingDate'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'taxWarehouseRegistration', ValueListProperty: 'taxWarehouseRegistration'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'materialNumber', ValueListProperty: 'materialNumber'},
				{ $Type: 'Common.ValueListParameterIn', LocalDataProperty: 'stockLedgerNumber', ValueListProperty: 'stockLedgerNumber'},
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'stockLedgerDivision', ValueListProperty: 'stockLedgerDivision'}
			],
			PresentationVariantQualifier: 'SLDivision'
		},
		Common.ValueList: { 
			CollectionPath: 'StockLedgerDivisionValueHelp',
			Label: 'Stock Ledger Division',
			SearchSupported: 'false',
			Parameters: [
				{ $Type: 'Common.ValueListParameterInOut', LocalDataProperty: 'stockLedgerDivision', ValueListProperty: 'id'},
				{ $Type: 'Common.ValueListParameterDisplayOnly', ValueListProperty: 'description'}
			]
		}
	);
	stockLedgerSubdivision @(
		sap.label: 'Stock Ledger Subdivision',
		Common.Label: 'Stock Ledger Subdivision'
	);
	alcoholicStrength @(
		sap.label: '% Vol.',
		Common.Label: '% Vol.'
	);
	taxWarehouseRegistrationOfManufacturingPlant @(
		sap.label: 'Tax Warehouse Registration of Manufacturing Plant',
		Common.Label: 'Tax Warehouse Registration of Manufacturing Plant'
	);
	exciseDutyNumberForTaxWarehouse @(
		sap.label: 'Excise Duty Number for Tax Warehouse',
		Common.Label: 'Excise Duty Number for Tax Warehouse'
	);
	externalTaxWarehouseRegistration @(
		sap.label: 'External Tax Warehouse Registration',
		Common.Label: 'External Tax Warehouse Registration'
	);
	externalExciseDutyNumber @(
		sap.label: 'External Excise Duty Number',
		Common.Label: 'External Excise Duty Number'
	);
	taxValueAmount @(
		sap.label: 'Tax Value Amount',
		Common.Label: 'Tax Value Amount'
	);
	taxValueCurrency @(
		sap.label: 'Tax Value Currency',
		Common.Label: 'Tax Value Currency'
	);
	salesOrderNumber @(
		sap.label: 'Sales Order Number',
		Common.Label: 'Sales Order Number'
	);
	salesOrderItem @(
		sap.label: 'Sales Order Item',
		Common.Label: 'Sales Order Item'
	);
};

annotate StockLedgerAlpService.StockLedgerLineItem with @(

// table columns ------------------------------------------------------------------------------------
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
	],
	
// filters ------------------------------------------------------------------------------------------
	
	UI.SelectionFields: [stockLedgerDivision, materialDocumentYear, materialDocumentPostingDate, taxWarehouseRegistration, materialNumber, stockLedgerNumber, baseUnitOfMeasure, taxValueCurrency],

// visual filter ------------------------------------------------------------------------------------

  UI.PresentationVariant#SLDivision: {
  		SortOrder: [ {
  			Property: taxValueAmount,
			Descending: true
		}],
		Visualizations: [
			'@UI.Chart#SLDivisionChart'
		]
	},
	
  UI.Chart#SLDivisionChart: { 
		Title: "Stock Ledger Division",
		Description: "Stock Ledger Division",
		ChartType: #Bar, 
		Dimensions: [ stockLedgerDivision ],
		Measures: [ taxValueAmount ],
		MeasureAttributes: [
			{
				Measure: taxValueAmount,
				Role: #Axis1
			}
		],
		DimensionAttributes: [
			{
				Dimension: "stockLedgerDivision",
				Role: #Category
			}
		]
	},

  UI.PresentationVariant#MNumber: {
  		SortOrder: [ {
  			Property: taxValueAmount,
			Descending: true
		}],
		Visualizations: [
			'@UI.Chart#MNumberChart'
		]
	},
	
  UI.Chart#MNumberChart: { 
		Title: "Material Number",
		Description: "Material Number",
		ChartType: #Bar, 
		Dimensions: [ materialNumber ],
		Measures: [ taxValueAmount ],
		MeasureAttributes: [
			{
				Measure: taxValueAmount,
				Role: #Axis1
			}
		],
		DimensionAttributes: [
			{
				Dimension: "materialNumber",
				Role: #Category
			}
		]
	},

	UI.PresentationVariant#TVAmount: {
  		SortOrder: [ {
  			Property: taxValueAmount,
			Descending: true
		}],
		Visualizations: [
			'@UI.Chart#TVAmountChart'
		]
	},
	
	UI.Chart#TVAmountChart: { 
		Title: "Tax Value Amount",
		Description: "Tax Value Amount",
		ChartType: #Line, 
		Dimensions: [ materialDocumentYear ],
		Measures: [ taxValueAmount ],
		MeasureAttributes: [
			{
				Measure: taxValueAmount,
				Role: #Axis1
			}
		],
		DimensionAttributes: [
			{
				Dimension: "materialDocumentYear",
				Role: #Category
			}
		]
	},
	
	UI.PresentationVariant#TWHRegistration: {
  		SortOrder: [ {
  			Property: taxValueAmount,
			Descending: true
		}],
		Visualizations: [
			'@UI.Chart#TWHRegistrationChart'
		]
	},
	
	UI.Chart#TWHRegistrationChart: { 
		Title: "Tax Warehouse Registration",
		Description: "Tax Warehouse Registration",
		ChartType: #Donut, 
		Dimensions: [ taxWarehouseRegistration ],
		Measures: [ taxValueAmount ],
		MeasureAttributes: [
			{
				Measure: taxValueAmount,
				Role: #Axis1
			}
		],
		DimensionAttributes: [
			{
				Dimension: "taxWarehouseRegistration",
				Role: #Category
			}
		]
	},

// ALP Chart and Table ------------------------------------------------------------------------------

  UI.PresentationVariant#DefaultPresentationVariant: {
  		SortOrder: [
  			{
  				Property: stockLedgerDivision,
				Descending: false
			},
			{
				Property: stockLedgerGroup,
				Descending: false
			}
		],
		Visualizations: [
			'@UI.Chart#ContentChart',
			'@UI.LineItem'
		]
	},
	
	UI.Chart#ContentChart: { 
		Title: "Stock Ledger Line Items",
		Description: "Bulk Chart",
		ChartType: #Bulk, 
		Dimensions: [ stockLedgerDivision ],
		Measures: [ taxValueAmount ],
		MeasureAttributes: [
			{
				Measure: taxValueAmount,
				Role: #Axis1,
				DataPoint: '@UI.DataPoint#ContentChartDataPoint'
			}
		],
		DimensionAttributes: [
			{
				Dimension: "stockLedgerDivision",
				Role: #Category
			}
		]
	},
	
	UI.DataPoint#ContentChartDataPoint: {
		Title: 'Stock Ledger Line Item',
		Value: taxValueAmount,
//		TargetValue: PredictedPrice
	},
	
// KPI ----------------------------------------------------------------------------------------------

    UI.SelectionPresentationVariant#KPI: {
		Text: 'KPI',
	  	PresentationVariant: "@UI.PresentationVariant#KPI",
	  	SelectionVariant: "@UI.SelectionVariant#KPI"
	},
	
	UI.SelectionVariant#KPI: {
		Parameters: [
//				Not needed but mandatory. So let's keep this empty for now
		]
	},

	UI.PresentationVariant#KPI: {
		Text: 'KPI something',
		SortOrder: [
  			{
  				Property: stockLedgerDivision,
				Descending: false
			}
		],
//		MaxItems: 10,
		Visualizations: [
			'@UI.Chart#KPI',
			'@UI.DataPoint#KPI'
		]
	},
	
	UI.Chart#KPI: { 
		Title: "Stock Ledger Line Items",
		Description: "Line Chart",
		ChartType: #Donut, 
		Dimensions: [ stockLedgerDivision ],
		Measures: [ taxValueAmount ],
		MeasureAttributes: [
			{
				Measure: taxValueAmount,
				Role: #Axis1,
				DataPoint: '@UI.DataPoint#KPIchart'
			}
		],
		DimensionAttributes: [
			{
				Dimension: "stockLedgerDivision",
				Role: #Category
			}
		]
	},
	
	UI.DataPoint#KPIchart: {
		Title: 'Stock Ledger Line Item',
		Value: taxValueAmount,
//		TargetValue: PredictedPrice
	},
	
	UI.DataPoint#KPI: {
		Title: 'Total Tax Value',
//		Description: ' ',
		Value: taxValueAmount,
//		TargetValue: taxValueAmount,
		Visualization: #Number,
		Criticality: #Positive,
		ValueFormat: {
  			ScaleFactor: 1000, //TODO: This is generated as Int but should be Decimal. Bug?
			NumberOfFractionalDigits: 1
		}
	},
	
);
