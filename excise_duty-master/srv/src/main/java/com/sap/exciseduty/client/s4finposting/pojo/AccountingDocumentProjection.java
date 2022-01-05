package com.sap.exciseduty.client.s4finposting.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountingDocumentProjection {

    protected static final String REFERENCE_OBJECT_TYPE = "ZMKPF";
    protected static final String POSTING_TEXT = "Excise Duty Posting";

    private String companyCode;
    private String glAccount1;
    private String glAccount2;
    private String plant;
    private LocalDate postingDate;
    private BigDecimal quantity;
    private String baseUnitOfMeasure;
    private BigDecimal taxAmount;
    private String taxCurrency;
    private String referenceMaterialDocumentYear;
    private String referenceMaterialDocumentNumber;
    private String referenceObjectSystem;
    private String businessAction;
    private String username;

    public AccountingDocumentProjection() {
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getGlAccount1() {
        return glAccount1;
    }

    public void setGlAccount1(String glAccount1) {
        this.glAccount1 = glAccount1;
    }

    public String getGlAccount2() {
        return glAccount2;
    }

    public void setGlAccount2(String glAccount2) {
        this.glAccount2 = glAccount2;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDate postingDate) {
        this.postingDate = postingDate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getBaseUnitOfMeasure() {
        return baseUnitOfMeasure;
    }

    public void setBaseUnitOfMeasure(String baseUnitOfMeasure) {
        this.baseUnitOfMeasure = baseUnitOfMeasure;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxCurrency() {
        return taxCurrency;
    }

    public void setTaxCurrency(String taxCurrency) {
        this.taxCurrency = taxCurrency;
    }

    public String getReferenceMaterialDocumentYear() {
        return referenceMaterialDocumentYear;
    }

    public void setReferenceMaterialDocumentYear(String referenceMaterialDocumentYear) {
        this.referenceMaterialDocumentYear = referenceMaterialDocumentYear;
    }

    public String getReferenceMaterialDocumentNumber() {
        return referenceMaterialDocumentNumber;
    }

    public void setReferenceMaterialDocumentNumber(String referenceMaterialDocumentNumber) {
        this.referenceMaterialDocumentNumber = referenceMaterialDocumentNumber;
    }

    public String getReferenceObjectSystem() {
        return referenceObjectSystem;
    }

    public void setReferenceObjectSystem(String referenceObjectSystem) {
        this.referenceObjectSystem = referenceObjectSystem;
    }

    public String getBusinessAction() {
        return businessAction;
    }

    public void setBusinessAction(String businessAction) {
        this.businessAction = businessAction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String serializeAsSOAPBody() {
        return AccountingDocumentProjectionSoapBody.of(this);
    }
}
