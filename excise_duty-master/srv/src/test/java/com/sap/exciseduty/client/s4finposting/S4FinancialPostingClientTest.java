package com.sap.exciseduty.client.s4finposting;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;

import com.sap.exciseduty.client.ClientFactory;
import com.sap.exciseduty.client.exception.S4AccountingDocumentPostingException;
import com.sap.exciseduty.client.s4finposting.S4AccountingDocumentPostingClient;
import com.sap.exciseduty.client.s4finposting.pojo.AccountingDocumentProjection;

public class S4FinancialPostingClientTest {

    S4AccountingDocumentPostingClient cut;

    @Before
    public void init() {

        cut = new S4AccountingDocumentPostingClient();
    }

    // @Test
    public void testExecute() {
        try {

            AccountingDocumentProjection accDoc = new AccountingDocumentProjection();

            accDoc.setCompanyCode("M201");
            accDoc.setPlant("M201");
            accDoc.setGlAccount1("0000175500");
            accDoc.setGlAccount2("0000468000");
            accDoc.setPostingDate(LocalDate.now());
            accDoc.setQuantity(new BigDecimal(1));
            accDoc.setBaseUnitOfMeasure("EA");
            accDoc.setTaxAmount(new BigDecimal(4.2));
            accDoc.setTaxCurrency("EUR");
            accDoc.setReferenceMaterialDocumentNumber("4900007144");
            accDoc.setReferenceMaterialDocumentYear("2017");
            accDoc.setReferenceObjectSystem("ER8CLNT070"); // TODO: Store in Config/read from inbound interface
            accDoc.setBusinessAction("RMWL");
            accDoc.setUsername("ZIMMERMANNDA"); // TODO: Take User from original material document once it's in the
                                                // inbound OData service

            System.out.println(cut.postAccountingDocument(accDoc));
        } catch (S4AccountingDocumentPostingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
