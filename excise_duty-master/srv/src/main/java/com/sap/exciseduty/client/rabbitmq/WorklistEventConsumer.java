package com.sap.exciseduty.client.rabbitmq;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import com.sap.cloud.sdk.cloudplatform.exception.ShouldNotHappenException;

import com.sap.exciseduty.client.ClientFactory;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.odata.actions.S4WorklistItemBuffer;
import com.sap.exciseduty.utility.ClientCredentialFlow;

public class WorklistEventConsumer extends DefaultConsumer {

    private static final String PORT = System.getenv("PORT");
    private static final String SERVICE_PATH = "/odata/v2/StockLedgerService/ProcessSingleWorklistItem";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WorklistEventConsumer(ClientFactory clientFactory, Channel channel) {
        super(channel);
        if (clientFactory == null) {
            throw new ShouldNotHappenException("illegal instantiation of " + this.getClass());
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

        String message = new String(body, "UTF-8");
        Gson gson = new Gson();
        S4WorklistItem worklistItem = gson.fromJson(message, S4WorklistItem.class);

        /*
         * TODO:
         * Workaround as we cannot open the Connected Cores context to interact with the generic provider and
         * Persistence
         */
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            S4WorklistItemBuffer.put(worklistItem);
            URIBuilder builder = new URIBuilder(getUri())
                    .addParameter("materialDocumentYear", "'" + worklistItem.getMaterialYear() + "'")
                    .addParameter("materialDocumentNumber", "'" + worklistItem.getMaterialDocumentNumber() + "'")
                    .addParameter("materialDocumentItem", "'" + worklistItem.getLine() + "'");

            HttpPost request = new HttpPost();
            request.setURI(builder.build());

            // We have to add the Authorization Context in ensure proper processing
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + ClientCredentialFlow.getOwnJwtToken());
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    logger.warn("Worklist item could not be processed: " + worklistItem.getMaterialYear() + " " + worklistItem.getMaterialDocumentNumber() + " " + worklistItem.getLine() + " with Status Code: "
                            + response.getStatusLine().getStatusCode());
                    // Not Acknowledge Message Processing
                    //TODO: Refine error message handling coming back from http request (only Nack for 500 errors)
                    super.getChannel().basicNack(envelope.getDeliveryTag(), false, true);

                } else {
                    logger.info("Event Received and processed for: " + worklistItem.getMaterialYear() + " " + worklistItem.getMaterialDocumentNumber() + " " + worklistItem.getLine());

                    // Acknowledge Message Processing
                    super.getChannel().basicAck(envelope.getDeliveryTag(), false);
                }
            }

        } catch (Exception e) {
            // Ensure that no exception is causing the Thread to break. Otherwise RabbitMQ will terminate the consumer
            // without a restart.
            e.printStackTrace();
            super.getChannel().basicNack(envelope.getDeliveryTag(), false, true);
        }
    }

    protected URI getUri() {
        URI uri = null;
        try {
            uri = new URI("http://localhost:" + PORT + SERVICE_PATH);
        } catch (URISyntaxException e) {
            logger.error("URI not valid with port: " + PORT);
        }
        return uri;
    }
}