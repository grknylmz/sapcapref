package com.sap.exciseduty.extpts;

import java.io.IOException;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;

public abstract class FaasExtensionPoint<Input, Output> implements ExtensionPoint<Input, Output> {

    //private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Destination destination;
    private final Class<Output> outputClass;

    public FaasExtensionPoint(Destination destination, Class<Output> outputClass) {
        this.outputClass = outputClass;
        this.destination = destination;
    }

    @Override
    public Output call(Input input, Extension<Input, Output> defaultImplementation) throws Exception {

        // we have a faas implementation?
        if (destination != null) {
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ExtensionPoint {}: invoking faas implementation for destination {} ...", getExtensionPointName(), destination.getName());
            return callFaasEndpoint(destination, input);
        }

        // we have a default implementation?
        if (defaultImplementation != null) {
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ExtensionPoint {}: invoking default implementation ...", getExtensionPointName());
            return defaultImplementation.call(input);
        }

        // we have neither faas nor default implementation
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ExtensionPoint {}: neither faas nor default implementation available", getExtensionPointName());
        return null;
    }

    private Output callFaasEndpoint(Destination destination, Input input) throws ExecutionException {

        try {
            URIBuilder builder = new URIBuilder(destination.getUri());

            String requestBody = GSON.toJson(input);
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ExtensionPoint {} input: {} ", getExtensionPointName(), formatJson(requestBody));

            StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);

            HttpPost request = new HttpPost(builder.build());
            request.setEntity(entity);

            request.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
            request.addHeader(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));

            HttpResponse response = HttpClientAccessor.getHttpClient(destination).execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseBody = EntityUtils.toString(response.getEntity());
                Output output = GSON.fromJson(responseBody, outputClass);
                logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ExtensionPoint {} output: {} ", getExtensionPointName(), formatJson(GSON.toJson(output)));
                return output;
            } else {
                String reason = response.getStatusLine().getReasonPhrase();
                logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ExtensionPoint {} error: {} ", getExtensionPointName(), reason);
                throw new ExecutionException(this, new IOException(reason));
            }
        } catch (Exception e) {
            throw new ExecutionException(this, e);
        }
    }

    public static String formatJson(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();
        String formattedJsonString = GSON.toJson(json);
        return formattedJsonString;
    }
}
