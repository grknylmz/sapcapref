using { demo.callcenter as call } from '../db/schema';

service CallCenterService {
    entity Inquiries as select from call.Inquiries
    actions {
        @sap.applicable.path: 'startEnabled'
        action start();
        @sap.applicable.path: 'closeEnabled'
        action close(satisfaction: Integer);
    }; 

    entity InquiryAnalytics as select from call.InquiryAnalytics{
        *,
        satisfaction.name as satisfactionText,
        3.0 as referenceValue: Decimal      
    };

    @readonly
    entity Category as select from call.Category;

    @readonly
    entity Status as select from call.Status;

    @readonly
    entity Satisfaction as select from call.Satisfaction;
} 

annotate CallCenterService.InquiryAnalytics with @
//Header annotations
(
    UI: {
        Chart: {
            $Type: 'UI.ChartDefinitionType',
            ChartType: #Donut,
            Measures: [count],
            MeasureAttributes: [{
                $Type: 'UI.ChartMeasureAttributeType',
                Measure: count,
                Role: #Axis1
            }],             
            Dimensions: [satisfaction_code],
            DimensionAttributes: [{
                $Type: 'UI.ChartDimensionAttributeType',
                Dimension: satisfaction_code,
                Role: #Category
            }]       
        },
        Chart#hoursBeforeStart: {
            $Type: 'UI.ChartDefinitionType',
            ChartType: #Column,
            Measures: [count],
            MeasureAttributes: [{
                $Type: 'UI.ChartMeasureAttributeType',
                Measure: count,
                Role: #Axis1
            }],             
            Dimensions: [hoursBefoerStart],
            DimensionAttributes: [{
                $Type: 'UI.ChartDimensionAttributeType',
                Dimension: hoursBefoerStart,
                Role: #Category
            }]       
        },   
        PresentationVariant#hoursBefoerStart: {
            $Type: 'UI.PresentationVariantType',
            SortOrder: [{Property : hoursBefoerStart}]
        },     
        DataPoint#satisfacton: {
            $Type: 'UI.DataPointType',
            Value: satisfactonAve_code,
            Title: 'Satisfaction',
            CriticalityCalculation: {
                $Type: 'UI.CriticalityCalculationType',
                ImprovementDirection: #Maximize,
                ToleranceRangeLowValue: 3,
                DeviationRangeLowValue: 2
            },
            TrendCalculation: {
                $Type: 'UI.TrendCalculationType',
                ReferenceValue: referenceValue,
                IsRelativeDifference: false,
                StrongUpDifference: 1.5,
                UpDifference: 1,
                DownDifference: -1,
                StrongDownDifference: -1.5
            },
            ValueFormat: {
                $Type: 'UI.NumberFormat',
                NumberOfFractionalDigits: 1
            }     
        }
    }
)

{
    satisfaction @(
        Common.Text: {
            $value                 : satisfactionText,
            ![@UI.TextArrangement] : #TextLast
        },        
    );
    count @(
        title: 'Count'
    );
}

annotate CallCenterService.Inquiries with @(
    UI: {
        HeaderInfo: {
            TypeName: 'Inquirey',
            TypeNamePlural: 'Inquires',
            Title: { Value: category.name }
        },        
        SelectionFields: [category2, category_code, status_code, satisfaction_code, createdAt],
        LineItem: [
            { $Type: 'UI.DataFieldForAction', Action: 'CallCenterService.EntityContainer/Inquiries_start', Label: 'Start',  Visible, Enabled},
            { $Type: 'UI.DataFieldForAction', Action: 'CallCenterService.EntityContainer/Inquiries_close', Label: 'Close',  Visible, Enabled},
            { Value: category_code },
            { Value: inquiry },
            { Value: status_code },
            { Value: startedAt },
            { Value: satisfaction_code },
            { Value: createdAt },
            { Value: modifiedAt }
        ],
        Facets: [
            {
                $Type: 'UI.CollectionFacet',
                Label: 'Detail',
                Facets: [
                    {$Type: 'UI.ReferenceFacet', Target: '@UI.FieldGroup#Main' }
                ]
            }
        ],        
        FieldGroup#Main: {
            Data: [
                { Value: category_code },
                { Value: inquiry },
                { Value: answer },
                { Value: status_code },
                { Value: satisfaction_code },         
            ]
        }
    }
)
//Field annotations
{
    category @(
        Common.ValueListWithFixedValues :true       
    );
    status @(
        Common.ValueListWithFixedValues :true       
    );
    satisfaction @(
        Common.ValueListWithFixedValues :true       
    );

}

annotate CallCenterService.Category with {
    code @(
        Common : {Text : {
            $value                 : name,
            ![@UI.TextArrangement] : #TextOnly
        }}
    )
}

annotate CallCenterService.Status with {
    code @(
        Common : {Text : {
            $value                 : name,
            ![@UI.TextArrangement] : #TextOnly
        }}
    )
}

annotate CallCenterService.Satisfaction with {
    code @(
        Common : {Text : {
            $value                 : name,
            ![@UI.TextArrangement] : #TextOnly
        }}
    )    
}