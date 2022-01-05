package com.sap.exciseduty.client.exception;

public class S4AccountingDocumentPostingException extends Exception {

    private static final long serialVersionUID = 4401806109953682129L;

    public S4AccountingDocumentPostingException(Exception e) {
        super(e);
    }

    public S4AccountingDocumentPostingException(String message) {
        super(message);
    }
}