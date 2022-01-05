package com.sap.exciseduty.utility;

import com.sap.cloud.sdk.service.prov.api.request.GenericRequest;

public final class RequestHelper {

    public static final String HTTP_HEADER_X_CSRF_TOKEN = "X-CSRF-Token";
    private static final String HTTP_HEADER_AUTHORIZATION = "authorization";

    public static String getAuthorizationHeader(GenericRequest request) {
        // "Bearer eyJhbGciOiJIUzI" --> [ "Bearer", "eyJhbGciOiJIUzI" ] --> "eyJhbGciOiJIUzI"
        return request.getHeader(HTTP_HEADER_AUTHORIZATION).split(" ")[1];
    }

}
