using erpDataTypes from './erpDataTypes';

@Common.Label: "Master Data"
@Analytics.visible: true
context MasterDataReplicationModel
{
    @Common.Label: "Units of Measure"
    @Analytics.visible: true
    entity UnitsOfMeasureForMaterial{ // MARM
        key materialNumber: erpDataTypes.MATNR;
        key alternativeUnitOfMeasure: erpDataTypes.UNIT;
            numeratorForConversionToBaseUnitsOfMeasure: erpDataTypes.UMREZ;
            denominatorForConversionToBaseUnitsOfMeasure: erpDataTypes.UMREN;
    }

    @Common.Label: "Customer"
    @Analytics.visible: true
    entity Customer{ // KNA1
        key customerNumber: erpDataTypes.KUNNR;
            countryKey: erpDataTypes.LAND1;
            name: erpDataTypes.NAME;
    }

    @Common.Label: "Material Description"
    @Analytics.visible: true
    entity MaterialDescription{ // MAKT
        key materialNumber: erpDataTypes.MATNR;
            // TODO: Add SPRAS
            description: erpDataTypes.TEXT40; //MAKTX
    }

    @Common.Label: "Country"
    @Analytics.visible: true
    entity Countries{ // T005
        key countryKey: erpDataTypes.LAND1;
            europeanUnionIndicator: Boolean; //XEGLD
    }
}