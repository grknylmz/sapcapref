package com.sap.exciseduty.client.rabbitmq.exception;

public class NoRabbitMQConnectionException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -4761768676293467065L;

    public NoRabbitMQConnectionException() {
        super();
    }

    public NoRabbitMQConnectionException(Exception e) {
        super(e);
    }

}
