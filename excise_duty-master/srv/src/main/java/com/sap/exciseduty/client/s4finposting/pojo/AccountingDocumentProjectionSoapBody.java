package com.sap.exciseduty.client.s4finposting.pojo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

// package private
class AccountingDocumentProjectionSoapBody {

    private static final String TEMPLATE_FILE_NAME = "AccountingDocumentProjectionSoapBody.xml";
    private static final String TEMPLATE_FILE_ENCODING = "UTF-8";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final BigDecimal MINUS_ONE = new BigDecimal(-1);

    public static String of(AccountingDocumentProjection adp) {
        String template = readTemplate();

        // ACCOUNTGL/item (1)
        template = template.replace("{GL_ACCOUNT_1}", adp.getGlAccount1());
        template = template.replace("{POSTING_TEXT}", AccountingDocumentProjection.POSTING_TEXT);
        template = template.replace("{COMPANY_CODE}", adp.getCompanyCode());
        template = template.replace("{PLANT}", adp.getPlant());
        template = template.replace("{POSTING_DATE_FORMATTED}", adp.getPostingDate().format(DATE_TIME_FORMATTER));
        template = template.replace("{QUANTITY}", adp.getQuantity().toString());
        template = template.replace("{UNIT_OF_MEASURE}", adp.getBaseUnitOfMeasure());

        // ACCOUNTGL/item (2)
        template = template.replace("{GL_ACCOUNT_2}", adp.getGlAccount2());

        // CURRENCYAMOUNT/item (1+2)
        template = template.replace("{TAX_CURRENCY}", adp.getTaxCurrency());
        template = template.replace("{TAX_AMOUNT_NEGATIVE}", adp.getTaxAmount().multiply(MINUS_ONE).setScale(2, RoundingMode.HALF_UP).toString());

        // CURRENCYAMOUNT/item (3+4)
        template = template.replace("{TAX_AMOUNT_POSITIVE}", adp.getTaxAmount().setScale(2, RoundingMode.HALF_UP).toString());

        // DOCUMENTHEADER
        template = template.replace("{REF_OBJECT_TYPE}", AccountingDocumentProjection.REFERENCE_OBJECT_TYPE);
        template = template.replace("{REF_MATERIAL_NUMBER_AND_YEAR}", adp.getReferenceMaterialDocumentNumber() + adp.getReferenceMaterialDocumentYear());
        template = template.replace("{REF_OBJECT_SYSTEM}", adp.getReferenceObjectSystem());
        template = template.replace("{BUSINESS_ACTION}", adp.getBusinessAction());
        template = template.replace("{USERNAME}", adp.getUsername());
        template = template.replace("{POSTING_DATE}", adp.getPostingDate().toString());

        return template;
    }

    private static String readTemplate() throws RuntimeException {
        try {
            String template = new String(Files.readAllBytes(Paths.get(AccountingDocumentProjectionSoapBody.class.getClassLoader().getResource(TEMPLATE_FILE_NAME).toURI())), TEMPLATE_FILE_ENCODING);
            return template;
        } catch (IOException | URISyntaxException e) {
            // TODO: discuss proper error handling
            throw new RuntimeException(e);
        }
    }
}
