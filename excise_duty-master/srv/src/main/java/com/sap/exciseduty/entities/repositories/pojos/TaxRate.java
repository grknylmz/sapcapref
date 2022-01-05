package com.sap.exciseduty.entities.repositories.pojos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxRate {

	private String country;
	private String exciseDutyTypeId;
	private String baseQuantityUnit;
	private LocalDate validFrom;
	private BigDecimal alcoholicStrengthLowerLimit;
	private BigDecimal taxRate;
	private String currency;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getExciseDutyTypeId() {
		return exciseDutyTypeId;
	}

	public void setExciseDutyTypeId(String exciseDutyTypeId) {
		this.exciseDutyTypeId = exciseDutyTypeId;
	}

	public String getBaseQuantityUnit() {
		return baseQuantityUnit;
	}

	public void setBaseQuantityUnit(String baseQuantityUnit) {
		this.baseQuantityUnit = baseQuantityUnit;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public BigDecimal getAlcoholicStrengthLowerLimit() {
		return alcoholicStrengthLowerLimit;
	}

	public void setAlcoholicStrengthLowerLimit(BigDecimal alcoholicStrengthLowerLimit) {
		this.alcoholicStrengthLowerLimit = alcoholicStrengthLowerLimit;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
