package com.sap.exciseduty.client.rabbitmq;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;
import com.sap.cloud.sdk.cloudplatform.CloudPlatformAccessor;
import com.sap.cloud.sdk.cloudplatform.ScpCfCloudPlatform;
import com.sap.cloud.sdk.cloudplatform.exception.ShouldNotHappenException;
import com.sap.exciseduty.client.ClientFactory;
import com.sap.exciseduty.client.exception.EventProcessingException;
import com.sap.exciseduty.client.rabbitmq.exception.NoRabbitMQConnectionException;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistStatusItem;
import com.sap.exciseduty.servlet.RabbitMQServletContextListener;

/**
 * This class is a helper class to parallelize WorklistItem processing. It is
 * responsible to enqueue a list of WorklistItems into an RabbitMQ queue. Rabbit
 * will then dispatch the enqueued WorkListItems to its consumers in a round
 * robin manner. Note that this class will also start the consumer (see
 * {@link #startEventConsumer()}). Spawning up several Excise Duty Java threads
 * will therefore allow to process the WorklistItems with multiple consumers.
 * Startup of the consumer is performed in
 * {@link RabbitMQServletContextListener}.
 *
 * WorklistEventOrchestrator is a singleton with lazy thread safe instantiation.
 */
public class WorklistEventOrchestrator {

    private static final String QUEUE_NAME = "WorklistEventQueue";
    private static final String QUEUE_NAME_STATUS = "WorklistStatusEventQueue";

    // The number has been determined by load tests with FIN posting where each
    // item has a processing time of ~250ms
    private static final int NUMBER_OF_CONSUMERS = 15;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Gson gson = new Gson();
    private Connection connection;

    protected WorklistEventOrchestrator() {
        /* prevent improper instantiation */ }

    /* ensure thread safe lazy instantiation */
    private static final class InstanceHolder {
        static final WorklistEventOrchestrator instance = new WorklistEventOrchestrator();
    }

    public static WorklistEventOrchestrator getInstance(ClientFactory clientFactory) {
        if (clientFactory == null) {
            throw new ShouldNotHappenException("Illegal instantiation of " + WorklistEventOrchestrator.class);
        }
        return InstanceHolder.instance;
    }

    public void startEventConsumer() throws NoRabbitMQConnectionException {
        try {
            for (int i = 0; i < NUMBER_OF_CONSUMERS; i++) {
                Channel channel = getChannel(QUEUE_NAME);
                Consumer consumer = ClientFactory.getWorklistEventConsumer(channel);
                channel.basicConsume(QUEUE_NAME, false, consumer);
            }
        } catch (final IOException e) {
            throw new NoRabbitMQConnectionException(e);
        }
    }

    public void sendEvent(List<S4WorklistItem> worklistItems) throws EventProcessingException, NoRabbitMQConnectionException {
        try {
            final Channel channel = getChannel(QUEUE_NAME);
            for (final S4WorklistItem worklistItem : worklistItems) {
                final String message = gson.toJson(worklistItem);
                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            }
            channel.close();
        } catch (final IOException | TimeoutException e) {
            throw new EventProcessingException(e);
        }
    }

    public void sendEvent(S4WorklistStatusItem statusItem) throws EventProcessingException, NoRabbitMQConnectionException {
        try {
            final Channel channel = getChannel(QUEUE_NAME_STATUS);
            final String message = gson.toJson(statusItem);
            channel.basicPublish("", QUEUE_NAME_STATUS, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            channel.close();
        } catch (final IOException | TimeoutException e) {
            throw new EventProcessingException(e);
        }
    }

    public GetResponse readStatusEventQueue() throws EventProcessingException, NoRabbitMQConnectionException {
        try {
            final Channel channel = getChannel(QUEUE_NAME_STATUS);
            GetResponse response = channel.basicGet(QUEUE_NAME_STATUS, true);
            channel.close();
            return response;
        } catch (final IOException | TimeoutException e) {
            throw new EventProcessingException(e);
        }
    }

    protected Channel getChannel(String Queue) throws IOException, NoRabbitMQConnectionException {
        if (connection == null) {
            connection = getConnection();
        }
        Channel channel = connection.createChannel();
        channel.queueDeclare(Queue, true, false, false, null);
        channel.basicQos(1);

        return channel;
    }

    protected Connection getConnection() throws NoRabbitMQConnectionException {
        Connection connection = null;

        final ScpCfCloudPlatform cloudPlatform = (ScpCfCloudPlatform) CloudPlatformAccessor.getCloudPlatform();
        //final JsonObject rabbitMqCredentials = cloudPlatform.getServiceCredentials("rabbitmq").orElse(null); //orElse needed with s4sdk 2.0.0
        final JsonObject rabbitMqCredentials = cloudPlatform.getServiceCredentials("rabbitmq");

        if (rabbitMqCredentials == null) {
            throw new NoRabbitMQConnectionException();
        }

        try {
            final ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(rabbitMqCredentials.get("uri").getAsString());

            // Default ThreadPool is CPUs * 2 which does not ensure a proper
            // processing of the queues, therefore we set it to the number of
            // consumers
            ExecutorService es = Executors.newFixedThreadPool(NUMBER_OF_CONSUMERS);
            connection = factory.newConnection(es);
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException | IOException | TimeoutException e) {
            if (e instanceof ConnectException) {
                logger.error("NO RABBITMQ Connection maintained");
                throw new NoRabbitMQConnectionException(e);
            }
            logger.error("RabbitMQ connection uri not valid: " + rabbitMqCredentials.get("uri").getAsString());
            throw new InternalServerErrorException(e);
        }
        return connection;
    }

    public void close() {
        if (this.connection != null && this.connection.isOpen()) {
            try {
                connection.close();
            } catch (IOException e) {
            }
        }
    }
}