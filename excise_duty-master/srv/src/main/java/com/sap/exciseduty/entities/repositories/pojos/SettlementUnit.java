package com.sap.exciseduty.entities.repositories.pojos;

public class SettlementUnit {

	private String companyCode;
	private String exciseDutyTypeId;
	private String settlementUnit;
	private String baseQuantityUnit;
	private Integer volumeDecimalPlaces;

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getExciseDutyTypeId() {
		return exciseDutyTypeId;
	}

	public void setExciseDutyTypeId(String exciseDutyTypeId) {
		this.exciseDutyTypeId = exciseDutyTypeId;
	}

	public String getSettlementUnit() {
		return settlementUnit;
	}

	public void setSettlementUnit(String settlementUnit) {
		this.settlementUnit = settlementUnit;
	}

	public String getBaseQuantityUnit() {
		return baseQuantityUnit;
	}

	public void setBaseQuantityUnit(String baseQuantityUnit) {
		this.baseQuantityUnit = baseQuantityUnit;
	}

	public Integer getVolumeDecimalPlaces() {
		return volumeDecimalPlaces;
	}

	public void setVolumeDecimalPlaces(Integer volumeDecimalPlaces) {
		this.volumeDecimalPlaces = volumeDecimalPlaces;
	}

}
