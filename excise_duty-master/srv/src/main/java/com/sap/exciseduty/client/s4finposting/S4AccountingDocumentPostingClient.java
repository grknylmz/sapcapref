package com.sap.exciseduty.client.s4finposting;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationAccessException;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;
import com.sap.exciseduty.client.exception.S4AccountingDocumentPostingException;
import com.sap.exciseduty.client.s4finposting.pojo.AccountingDocumentProjection;

public class S4AccountingDocumentPostingClient {

    private static final String HTTP_DESTINATION = "ExciseDuty-S4-onPremise";
    private static final String SERVICE_PATH = "/sap/bc/srt/rfc/sap/ZED_ACC_DOC_POST_AND_COMMIT/070/ZED_ACC_DOC_POST_AND_COMMIT/ZED_ACC_DOC_POST_AND_COMMIT";
    private static final Pattern RESULT_PATTERN = Pattern.compile("^.*<NUMBER>605</NUMBER>\\s*<MESSAGE>\\s*Document posted successfully: \\S+ ([\\S]*) \\S{10}</MESSAGE>.*");

    private final Destination destination;

    public S4AccountingDocumentPostingClient() throws DestinationNotFoundException, DestinationAccessException {
        destination = DestinationAccessor.getDestination(HTTP_DESTINATION);
    }

    public String postAccountingDocument(AccountingDocumentProjection posting) throws S4AccountingDocumentPostingException {

        final URIBuilder builder = new URIBuilder(destination.getUri()).setPath(SERVICE_PATH);

        final String entityBody = posting.serializeAsSOAPBody();
        final StringEntity entity = new StringEntity(entityBody, ContentType.APPLICATION_XML);

        try {
            final HttpPost request = new HttpPost(builder.build());
            request.setEntity(entity);

            request.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/soap+xml"));

            final HttpResponse response = HttpClientAccessor.getHttpClient(destination).execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                final String responseEntity = EntityUtils.toString(response.getEntity());
                final Matcher matcher = RESULT_PATTERN.matcher(responseEntity);
                if (matcher.matches()) {
                    return matcher.group(1);
                } else {
                    throw new S4AccountingDocumentPostingException("SOAP request successful but business error occured");
                }
            } else {
                throw new S4AccountingDocumentPostingException("SOAP HTTP Request failed: " + response.getStatusLine().getReasonPhrase());
            }
        } catch (URISyntaxException | ParseException | IOException e) {
            throw new S4AccountingDocumentPostingException(e);
        }
    }

    public void cancelAccountingDocument(String referenceMaterialDocumentYear, String referenceMaterialDocumentNumber, String referenceObjectSystem, String businessAction) {
        /*
         * TODO: Consume an Interface that determines any existing accounting document for a given combination of
         * OBJ_TYPE (=ZMKPF), OBJ_KEY (=MatDocNumber+MatDocYear), OBJ_SYS, BUS_ACT. Afterwards, cancel each such
         * document. Could also be two separate
         * methods.
         */
    }

}
