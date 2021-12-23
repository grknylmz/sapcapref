

////////////////////////////////////////////////////////////////////////////
//
//	Note: this is designed for the OrdersService being co-located with
//	bookshop. It does not work if OrdersService is run as a separate
// 	process, and is not intended to do so.
//
////////////////////////////////////////////////////////////////////////////



using { ibeService } from '../../srv/ibe-service';


@odata.draft.enabled
annotate ibeService.ibe with @(
	UI: {
		SelectionFields: [ createdAt, createdBy ],
		LineItem: [
			{Value: ReqNo, Label:'Requirement No#'},
            {Value: buyer, Label:'Customer'},
			{Value: createdAt, Label:'Date'},
			{Value: vehicleMake, Label:'Make'},
			{Value: DocStatus, Label: 'Status' },
            {Value: url, Label: 'url'}
		],
		HeaderInfo: {
			TypeName: 'ReqNo', TypeNamePlural: 'Requirements',
			Title: {
				Label: 'requirement number ', //A label is possible but it is not considered on the ObjectPage yet
				Value: ReqNo
			},
			Description: {Value: createdBy}
		},
		Identification: [ //Is the main field group
			{Value: createdBy, Label:'Customer'},
			{Value: createdAt, Label:'Date'},
			{Value: ReqNo, Label: 'ReqNo' },
            {Value: url, ![@Core.Links] : [
                {
                    $Type : 'Core.Link',
                    rel : url,
                    href : url,
                },
            ], }
		],
		HeaderFacets: [
			{$Type: 'UI.ReferenceFacet', Label: '{i18n>Created}', Target: '@UI.FieldGroup#Created'},
			{$Type: 'UI.ReferenceFacet', Label: '{i18n>Modified}', Target: '@UI.FieldGroup#Modified'},
		],
		Facets: [
			{$Type: 'UI.ReferenceFacet', Label: '{i18n>Details}', Target: '@UI.FieldGroup#Details'},
			{$Type: 'UI.ReferenceFacet', Label: '{i18n>OrderItems}', Target: 'Items/@UI.LineItem'},
		],
		FieldGroup#Details: {
			Data: [
				{Value: currency.code, Label:'Currency'}
			]
		},
		FieldGroup#Created: {
			Data: [
				{Value: createdBy},
				{Value: createdAt},
			]
		},
		FieldGroup#Modified: {
			Data: [
				{Value: modifiedBy},
				{Value: modifiedAt},
			]
		},
	},
) {
	createdAt @UI.HiddenFilter:false;
	createdBy @UI.HiddenFilter:false;
};


annotate ibeService.ibeReq_Items with @(
	UI: {
		LineItem: [
			{Value: fetaureName, Label:'Feature'},
			{Value: fetaureType, Label:'Feature Type'},
			{Value: price, Label:'Unit Price'},
			{Value: comment, Label:'comments'},
            {Value: url, ![@Core.Links]: [
                {
                    $Type : 'Core.Link',
                    rel : url,
                    href : url,
                },
            ], },
		],
		Identification: [ //Is the main field group
			{Value: fetaureName, Label:'Feature'},
			{Value: price, Label:'Unit Price'},
		],
		Facets: [
			{$Type: 'UI.ReferenceFacet', Label: '{i18n>OrderItems}', Target: '@UI.Identification'},
		],
	},
) {
	amount @(
		Common.FieldControl: #Mandatory
	);
};