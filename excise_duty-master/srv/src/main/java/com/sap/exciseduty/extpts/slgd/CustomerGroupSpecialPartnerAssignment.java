package com.sap.exciseduty.extpts.slgd;

public class CustomerGroupSpecialPartnerAssignment {

    private String customerGroup;
    private SpecialPartnerType specialPartnerType = new SpecialPartnerType();

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public SpecialPartnerType getSpecialPartnerType() {
        return specialPartnerType;
    }

    public void setSpecialPartnerType(SpecialPartnerType specialPartnerType) {
        this.specialPartnerType = specialPartnerType;
    }

}
