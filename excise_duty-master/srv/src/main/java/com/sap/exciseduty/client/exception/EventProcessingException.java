package com.sap.exciseduty.client.exception;

public class EventProcessingException extends Exception {

    private static final long serialVersionUID = -3517708029397633597L;

    public EventProcessingException(Exception e) {
        super(e);
    }
}
