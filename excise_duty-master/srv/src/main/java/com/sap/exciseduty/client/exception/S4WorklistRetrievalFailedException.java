package com.sap.exciseduty.client.exception;

public class S4WorklistRetrievalFailedException extends Exception {

    private static final long serialVersionUID = 985844394163580792L;

    public S4WorklistRetrievalFailedException() {
        super();
    }

    public S4WorklistRetrievalFailedException(Exception e) {
        super(e);
    }
}
