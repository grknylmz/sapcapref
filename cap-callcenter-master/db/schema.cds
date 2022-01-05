namespace demo.callcenter;
using { cuid, managed, sap.common } from '@sap/cds/common';

entity Inquiries: cuid, managed{
    category: Association to Category @title: 'Category';
    inquiry: String(1000) @title: 'Inquiry';
    startedAt: DateTime @title: 'Started At'; 
    answer: String(1000) @title: 'Answer';
    status: Association to Status @title: 'Status';
    satisfaction: Association to  Satisfaction @title: 'Satisfaction';
    virtual startEnabled: Boolean;
    virtual closeEnabled: Boolean;    
    hoursBefoerStart: Decimal;
}

@Aggregation.ApplySupported.PropertyRestrictions: true
view InquiryAnalytics as select from Inquiries{
        key ID,
        @Analytics.Dimension: true
        category,
        @Analytics.Dimension: true
        satisfaction,
        @Analytics.Measure: true
        @Aggregation.default: #AVG  
        satisfaction as satisfactonAve,
        @Analytics.Measure: true
        @Aggregation.default: #SUM 
        1 as count: Integer,
        createdAt,
        startedAt,
        @Analytics.Dimension: true
        hoursBefoerStart
    } where status.code = '3';

entity Category: common.CodeList {
    key code   : String(1)
}

entity Status: common.CodeList {
    key code: String(1)
}

entity Satisfaction: common.CodeList {
    key code: Integer
}