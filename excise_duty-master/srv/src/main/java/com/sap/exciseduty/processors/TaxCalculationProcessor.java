package com.sap.exciseduty.processors;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.exciseduty.entities.repositories.TaxRateRepository;
import com.sap.exciseduty.entities.repositories.UnitsOfMeasureForMaterialRepository;
import com.sap.exciseduty.entities.repositories.exceptions.EntityNotFoundException;
import com.sap.exciseduty.entities.repositories.pojos.MaterialMasterExtension;
import com.sap.exciseduty.entities.repositories.pojos.SettlementUnit;
import com.sap.exciseduty.entities.repositories.pojos.StockLedgerLineItem;
import com.sap.exciseduty.entities.repositories.pojos.TaxRate;
import com.sap.exciseduty.entities.repositories.pojos.UnitsOfMeasureForMaterial;
import com.sap.exciseduty.processors.exceptions.ConvertionToSettlementUnitNotPossibleException;
import com.sap.exciseduty.processors.exceptions.TaxRateNotFoundException;
import com.sap.exciseduty.processors.pojos.TaxCalculationInput;
import com.sap.exciseduty.processors.pojos.TaxCalculationResult;

public class TaxCalculationProcessor {

    private final DataSourceHandler handler;

    public TaxCalculationProcessor(DataSourceHandler handler) {
        this.handler = handler;
    }

    public void execute(TaxCalculationInput input, StockLedgerLineItem stockLedgerLineItem) throws TaxRateNotFoundException, ConvertionToSettlementUnitNotPossibleException {
        TaxCalculationResult taxCalculationResult = execute(input);

        stockLedgerLineItem.setTaxValueAmount(taxCalculationResult.getTaxValueAmount());
        stockLedgerLineItem.setTaxValueCurrency(taxCalculationResult.getTaxValueCurrency());
    }

    public TaxCalculationResult execute(TaxCalculationInput input) throws TaxRateNotFoundException, ConvertionToSettlementUnitNotPossibleException {
        /*
         * Convert quantity to SettlementUnit
         */
        BigDecimal quantityInSettlementUnit = convertQuantityToSettlementUnit(input.getMaterialNumber(), input.getSettlementUnit(), input.getMaterialDocumentQuantity(), input.getMaterialDocumentBaseUnitOfMeasure());

        /*
         * Read Tax Rate
         */
        // TODO: how to get Country for the TaxRate
        TaxRate taxRate = getTaxRate("DE",
                input.getMaterialMasterExtension().getExciseDutyTypeId(),
                input.getSettlementUnit().getBaseQuantityUnit(),
                input.getMaterialDocumentPostingDate(),
                input.getMaterialMasterExtension().getAlcoholicStrength());

        /*
         * Calculate Tax value amount
         */
        BigDecimal taxValueAmount;
        if (input.getBusinessTransactionType() != null) {
            switch (input.getBusinessTransactionType()) {
                case con_versteuert:
                case con_eu:
                case con_steuerlager:
                case con_steuerlager_eu:
                case con_drittland:
                    taxValueAmount = quantityInSettlementUnit.multiply(taxRate.getTaxRate()).multiply(input.getMaterialMasterExtension().getAlcoholicStrength());
                    break;
                default:
                    taxValueAmount = new BigDecimal(0);
            }
        } else {
            taxValueAmount = quantityInSettlementUnit.multiply(taxRate.getTaxRate()).multiply(input.getMaterialMasterExtension().getAlcoholicStrength());
        }

        /*
         * Prepare Result
         */
        taxValueAmount = taxValueAmount.setScale(5, BigDecimal.ROUND_HALF_UP);
        TaxCalculationResult taxCalculationResult = prepareTaxCalculationResult(input.getMaterialMasterExtension(), taxRate, taxValueAmount);

        return taxCalculationResult;
    }

    protected BigDecimal convertQuantityToSettlementUnit(String materialNumber, SettlementUnit settlementUnit, BigDecimal lineItemQuantity, String lineItemBaseUnitOfMeasure) throws ConvertionToSettlementUnitNotPossibleException {
        if (settlementUnit.getBaseQuantityUnit().toUpperCase().equals(lineItemBaseUnitOfMeasure.toUpperCase())) {
            return lineItemQuantity;
        }

        // Get conversion rate for quantity
        UnitsOfMeasureForMaterial unitOfMeasureForMaterial;
        try {
            unitOfMeasureForMaterial = getUnitOfMeasureConvertionForMaterial(materialNumber, settlementUnit.getBaseQuantityUnit());
        } catch (EntityNotFoundException e) {
            throw new ConvertionToSettlementUnitNotPossibleException();
        }

        BigDecimal result;
        BigDecimal multiplier = unitOfMeasureForMaterial.getDenominatorForConversionToBaseUnitsOfMeasure().divide(unitOfMeasureForMaterial.getNumeratorForConversionToBaseUnitsOfMeasure());
        result = lineItemQuantity.multiply(multiplier);

        return result;
    }

    protected TaxCalculationResult prepareTaxCalculationResult(MaterialMasterExtension materialMasterExtension, TaxRate taxRate, BigDecimal taxValueAmount) {
        TaxCalculationResult result = new TaxCalculationResult();

        result.setExciseDutyTypeId(materialMasterExtension.getExciseDutyTypeId());
        result.setTaxValueAmount(taxValueAmount);
        result.setTaxValueCurrency(taxRate.getCurrency());

        return result;
    }

    protected TaxRate getTaxRate(String country, String exciseDutyTypeId, String baseQuantityUnit, LocalDate validFrom, BigDecimal alcoholicStength) throws TaxRateNotFoundException {
        TaxRateRepository repo = new TaxRateRepository(handler);
        TaxRate result;
        try {
            result = repo.getByKeyAndMatchingAlcoholicStrength(country, exciseDutyTypeId, baseQuantityUnit, validFrom, alcoholicStength);
        } catch (EntityNotFoundException e) {
            throw new TaxRateNotFoundException();
        }

        if (result == null || result.getAlcoholicStrengthLowerLimit() == null) {
            throw new TaxRateNotFoundException();
        }
        return result;
    }

    protected UnitsOfMeasureForMaterial getUnitOfMeasureConvertionForMaterial(String materialNumber, String alternativeUnitOfMeasure) throws EntityNotFoundException {
        UnitsOfMeasureForMaterialRepository repo = new UnitsOfMeasureForMaterialRepository(handler);
        UnitsOfMeasureForMaterial result = repo.getByAlternativeUnitOfMeasure(materialNumber, alternativeUnitOfMeasure);
        return result;
    }

}
