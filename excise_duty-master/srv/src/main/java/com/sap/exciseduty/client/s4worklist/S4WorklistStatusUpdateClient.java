package com.sap.exciseduty.client.s4worklist;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationAccessException;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;
import com.sap.exciseduty.client.exception.S4WorklistStatusUpdateFailedException;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;
import com.sap.exciseduty.utility.EnvironmentHelper;
import com.sap.exciseduty.utility.RequestHelper;

public class S4WorklistStatusUpdateClient {

    private final String HTTP_DESTINATION = "ExciseDuty-S4-onPremise";
    private final String SERVICE_PATH = EnvironmentHelper.getVariableWithDefault("SERVICE_PATH_S4_WORKLIST_STATUS_UPDATE", "/sap/opu/odata/sap/Z_ED_STATUPD_RAP_SRV/IS_ED_UPDATE");

    private Destination destination;

    public S4WorklistStatusUpdateClient() throws DestinationNotFoundException, DestinationAccessException {
        destination = DestinationAccessor.getDestination(HTTP_DESTINATION);
    }

    public void updateWorklistItemStatus(S4WorklistItem worklistItem, String targetStatus) throws S4WorklistStatusUpdateFailedException {
        updateWorklistItemStatus(Arrays.asList(worklistItem), targetStatus);
    }

    public void updateWorklistItemStatus(List<S4WorklistItem> worklistItems, String targetStatus) throws S4WorklistStatusUpdateFailedException {
        StringBuilder bodyBuilder = new StringBuilder();
        String batchKey = UUID.randomUUID().toString();
        String changeSetKey = UUID.randomUUID().toString();

        String entityBody = "{  \"stats\": \"" + targetStatus + "\" }";

        bodyBuilder.append("--batch_" + batchKey + System.lineSeparator());
        bodyBuilder.append("Content-Type: multipart/mixed; boundary=changeset_" + changeSetKey + System.lineSeparator());
        bodyBuilder.append(System.lineSeparator());

        for (S4WorklistItem worklistItem : worklistItems) {
            bodyBuilder.append("--changeset_" + changeSetKey + System.lineSeparator());
            bodyBuilder.append("Content-Type: application/http" + System.lineSeparator());
            bodyBuilder.append(System.lineSeparator());
            bodyBuilder.append(
                    String.format("PATCH IS_ED_UPDATE(mblnr='%s',mjahr='%s',zeile='%s',stlkn='%s') HTTP/1.1",
                            worklistItem.getMaterialDocumentNumber(),
                            worklistItem.getMaterialYear(),
                            worklistItem.getLine(),
                            worklistItem.getBillOfMaterialItemNodeNumber()) + System.lineSeparator());
            bodyBuilder.append("Content-Type: application/json" + System.lineSeparator());
            bodyBuilder.append(System.lineSeparator());
            bodyBuilder.append(entityBody + System.lineSeparator());
        }
        bodyBuilder.append("--changeset_" + changeSetKey + "--" + System.lineSeparator());
        bodyBuilder.append("--batch_" + batchKey + "--");

        URIBuilder builder = new URIBuilder(destination.getUri()).setPath(SERVICE_PATH.replace("IS_ED_UPDATE", "$batch"));

        StringEntity entity = new StringEntity(bodyBuilder.toString(), "UTF-8");
        try {
            final HttpPost request = new HttpPost(builder.build());
            getCsrfHeaders().forEach(request::addHeader);
            request.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "multipart/mixed;boundary=batch_" + batchKey));
            request.setEntity(entity);

            HttpClient client = HttpClientAccessor.getHttpClient(destination);
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            int matches = StringUtils.countMatches(responseBody, "HTTP/1.1 204 No Content");

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_ACCEPTED || matches != worklistItems.size()) {
                throw new S4WorklistStatusUpdateFailedException();
            }
        } catch (URISyntaxException | IOException | ParseException e) {
            throw new S4WorklistStatusUpdateFailedException(e);
        }
    }

    protected ArrayList<Header> getCsrfHeaders() throws S4WorklistStatusUpdateFailedException {

        HttpResponse response = requestCsrfToken();
        return parseHeaders(response);
    }

    private ArrayList<Header> parseHeaders(HttpResponse response) {
        ArrayList<Header> headers = new ArrayList<>();
        Header xCSRFTokenHeader = response.getFirstHeader(RequestHelper.HTTP_HEADER_X_CSRF_TOKEN);
        headers.add(new BasicHeader(RequestHelper.HTTP_HEADER_X_CSRF_TOKEN, xCSRFTokenHeader.getValue()));

        String cookieValues = Arrays.stream(response.getAllHeaders())
                .filter(header -> header.getName().contains("set-cookie"))
                .flatMap(setCookieHeader -> HttpCookie.parse(setCookieHeader.toString()).stream())
                .map(cookie -> cookie.getName().concat("=").concat(cookie.getValue())).collect(Collectors.joining("; "));

        headers.add(new BasicHeader("Cookie", cookieValues));
        return headers;
    }

    private HttpResponse requestCsrfToken() throws S4WorklistStatusUpdateFailedException {
        URIBuilder builder = new URIBuilder(destination.getUri()).setPath(SERVICE_PATH);

        try {
            final HttpHead request = new HttpHead(builder.build());

            request.addHeader(new BasicHeader(RequestHelper.HTTP_HEADER_X_CSRF_TOKEN, "fetch"));
            request.addHeader(new BasicHeader("DataServiceVersion", "2.0"));
            request.addHeader(new BasicHeader("MaxDataServiceVersion", "2.0"));

            return HttpClientAccessor.getHttpClient(destination).execute(request);

        } catch (URISyntaxException | IOException e) {
            throw new S4WorklistStatusUpdateFailedException(e);
        }
    }
}
