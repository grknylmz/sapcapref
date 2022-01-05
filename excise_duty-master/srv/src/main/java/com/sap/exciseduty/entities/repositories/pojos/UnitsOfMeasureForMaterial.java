package com.sap.exciseduty.entities.repositories.pojos;

import java.math.BigDecimal;

public class UnitsOfMeasureForMaterial {

    private String materialNumber;
    private String alternativeUnitOfMeasure;

    private BigDecimal numeratorForConversionToBaseUnitsOfMeasure;
    private BigDecimal denominatorForConversionToBaseUnitsOfMeasure;

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getAlternativeUnitOfMeasure() {
        return alternativeUnitOfMeasure;
    }

    public void setAlternativeUnitOfMeasure(String alternativeUnitOfMeasure) {
        this.alternativeUnitOfMeasure = alternativeUnitOfMeasure;
    }

    public BigDecimal getNumeratorForConversionToBaseUnitsOfMeasure() {
        return numeratorForConversionToBaseUnitsOfMeasure;
    }

    public void setNumeratorForConversionToBaseUnitsOfMeasure(BigDecimal numeratorForConversionToBaseUnitsOfMeasure) {
        this.numeratorForConversionToBaseUnitsOfMeasure = numeratorForConversionToBaseUnitsOfMeasure;
    }

    public BigDecimal getDenominatorForConversionToBaseUnitsOfMeasure() {
        return denominatorForConversionToBaseUnitsOfMeasure;
    }

    public void setDenominatorForConversionToBaseUnitsOfMeasure(BigDecimal denominatorForConversionToBaseUnitsOfMeasure) {
        this.denominatorForConversionToBaseUnitsOfMeasure = denominatorForConversionToBaseUnitsOfMeasure;
    }

}
