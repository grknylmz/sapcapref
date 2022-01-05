package com.sap.exciseduty.servlet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sap.exciseduty.utility.ClientCredentialFlow;

public class HealthCheckServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 5488894804182826218L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean issuesFound = false;
    private String authToken;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.issuesFound = false;

        // Retrieve Token for further processing
        try {
            authToken = "Bearer " + ClientCredentialFlow.getOwnJwtToken();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        resp.setContentType(ContentType.APPLICATION_JSON.toString());
        JsonObject result = new JsonObject();

        List<String> oDataServices = getOdataServices(req);
        List<String> edmxServiceFiles = getEdmxServiceFiles();

        // result.addProperty("Verify edmx file count", validate(edmxServiceFiles.size() == 11));
        result.addProperty("Verify oData service count", validate(oDataServices.size() == 11));
        result.add("Verify oData service metadata", verifyMetaData(oDataServices));

        PrintWriter out = resp.getWriter();
        out.print(result);

        if (this.issuesFound) {
            logger.error("Health Check found severe issues");
            Gson gson = new Gson();
            logger.error(gson.toJson(result));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
        }

    }

    private List<String> getEdmxServiceFiles() {
        File file = new File(getClass().getClassLoader().getResource("edmx/").getFile());
        final FilenameFilter filter = (dir, name) -> name.toLowerCase().endsWith(".xml");

        return new ArrayList<String>(Arrays.asList(file.list(filter)));
    }

    private List<String> getOdataServices(HttpServletRequest req) {
        Pattern htmltag = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1");
        List<String> links = new ArrayList<String>();

        String reqURL = req.getRequestURL().substring(0, req.getRequestURL().length() - 6);
        reqURL = reqURL + "odata/v2";
        URIBuilder builder;
        try {
            builder = new URIBuilder(reqURL).setScheme("http").setHost("localhost").setPort((Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8080"))));

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet();
                request.setURI(builder.build());
                request.setHeader(HttpHeaders.AUTHORIZATION, authToken);

                CloseableHttpResponse response = httpClient.execute(request);

                String resultHTML = EntityUtils.toString(response.getEntity());
                response.close();

                Matcher tagMatch = htmltag.matcher(resultHTML);
                while (tagMatch.find()) {
                    links.add(tagMatch.group(2));
                }
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return links;
    }

    private JsonObject verifyMetaData(List<String> serviceUrl) {

        JsonObject result = new JsonObject();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            for (String service : serviceUrl) {
                JsonObject serviceResult = new JsonObject();

                try {
                    URIBuilder builder = new URIBuilder(service + "/$metadata").setScheme("http").setHost("localhost").setPort((Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8080"))));

                    HttpGet request = new HttpGet();
                    request.setURI(builder.build());
                    request.setHeader(HttpHeaders.AUTHORIZATION, authToken);

                    CloseableHttpResponse response = httpClient.execute(request);

                    serviceResult.addProperty("$metadata reachable", validate(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK));
                    String resultXML = EntityUtils.toString(response.getEntity());
                    serviceResult.addProperty("$metadata contains references", validate(resultXML.contains("<edmx:Reference")));
                    response.close();

                    result.add(service.substring(service.lastIndexOf("/") + 1), serviceResult);

                } catch (URISyntaxException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return result;
    }

    private String validate(boolean value) {
        if (!value) {
            this.issuesFound = true;
            return "FAILED";
        }
        return "OK";
    }

}