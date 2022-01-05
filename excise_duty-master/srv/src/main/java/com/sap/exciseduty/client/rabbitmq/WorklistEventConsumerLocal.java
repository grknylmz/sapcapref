package com.sap.exciseduty.client.rabbitmq;

import java.net.URI;
import java.net.URISyntaxException;

import com.rabbitmq.client.Channel;
import com.sap.exciseduty.client.ClientFactory;

public class WorklistEventConsumerLocal extends WorklistEventConsumer {

    public WorklistEventConsumerLocal(ClientFactory clientFactory, Channel channel) {
        super(clientFactory, channel);
    }

    @Override
    protected URI getUri() {

        URI uri = null;
        try {
            uri = new URI("http://localhost:8080/exciseDuty-java/odata/v2/StockLedgerService/ProcessSingleWorklistItem");
        } catch (URISyntaxException e) {
            throw new InternalError();
        }
        return uri;
    }
}
