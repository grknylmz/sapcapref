using StockLedgerProcessingErrorService from './StockLedgerProcessingErrorService'; 

annotate StockLedgerProcessingErrorService.StockLedgerProcessingError with {
	materialDocumentYear
		@Common.Label: 'Material Document Year'
		@Common.FieldControl: #ReadOnly;
	materialDocumentNumber
		@Common.Label: 'Material Document Number'
		@Common.FieldControl: #ReadOnly;
	materialDocumentItem
		@Common.Label: 'Document Item'
		@Common.FieldControl: #ReadOnly;
	exciseDutyPositionNumber
		@Common.Label: 'Duty Position Number'
		@Common.FieldControl: #ReadOnly;
	companyCode
		@Common.Label: 'Company Code';
	plant
		@Common.Label: 'Plant';
	storageLocation
		@Common.Label: 'Storage Location';
	materialDocumentPostingDate
		@Common.Label: 'Material Document Posting Date';
	materialNumber
		@Common.Label: 'Material Number';
	errorText
		@Common.Label: 'Processing Error Description';
};

// List Report columns

annotate StockLedgerProcessingErrorService.StockLedgerProcessingError with @(
	UI.LineItem: [ 
		{$Type: 'UI.DataFieldForAction', Label: 'Reprocess', Action: 'StockLedgerProcessingError/ReprocessItem'},
		{$Type: 'UI.DataField', Value: materialDocumentYear},
		{$Type: 'UI.DataField', Value: materialDocumentNumber},
		{$Type: 'UI.DataField', Value: materialDocumentItem},
		{$Type: 'UI.DataField', Value: exciseDutyPositionNumber},
		{$Type: 'UI.DataField', Value: companyCode},
		{$Type: 'UI.DataField', Value: plant},
		{$Type: 'UI.DataField', Value: storageLocation},
		{$Type: 'UI.DataField', Value: materialDocumentPostingDate},
		{$Type: 'UI.DataField', Value: materialNumber},
		{$Type: 'UI.DataField', Value: errorText}
		
	],
	
	UI.HeaderInfo: {
		Title: { Value: description },
		TypeName:'Stock Ledger Posting Processing Error', 
        TypeNamePlural:'Stock Ledger Posting Processing Errors'
	}

);