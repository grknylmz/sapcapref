package com.sap.exciseduty.client.rabbitmq;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sap.exciseduty.client.ClientFactory;
import com.sap.exciseduty.client.rabbitmq.exception.NoRabbitMQConnectionException;

public class WorklistEventOrchestratorLocal extends WorklistEventOrchestrator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final WorklistEventOrchestratorLocal instance = new WorklistEventOrchestratorLocal();

    protected WorklistEventOrchestratorLocal() {
        /* prevent improper instantiation */ }

    public static WorklistEventOrchestratorLocal getInstance(ClientFactory clientFactory) {
        if (clientFactory == null) {
            throw new RuntimeException("illegal instantiation of " + WorklistEventOrchestratorLocal.class);
        }
        return instance;
    }

    @Override
    protected Connection getConnection() throws NoRabbitMQConnectionException {
        Connection connection = null;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            if (e instanceof ConnectException) {
                logger.error("NO RABBITMQ Connection maintained");
                throw new NoRabbitMQConnectionException(e);
            }
            e.printStackTrace();
        }
        return connection;
    }
}
