package com.sap.exciseduty.processors.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessingException extends Exception {

    private final static Logger logger = LoggerFactory.getLogger(ProcessingException.class);

    private static final long serialVersionUID = 8141057006540060070L;

    public ProcessingException() {
        super();
    }

    public ProcessingException(String message) {
        super(message);
        logger.info(message);
    }

}
