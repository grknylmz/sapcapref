package com.sap.exciseduty.workaround;

import com.google.gson.JsonObject;
import com.sap.cloud.sdk.cloudplatform.ScpCfCloudPlatform;
import com.sap.cloud.sdk.cloudplatform.exception.CloudPlatformException;
import com.sap.cloud.sdk.cloudplatform.exception.MultipleServiceBindingsException;
import com.sap.cloud.sdk.cloudplatform.exception.NoServiceBindingException;

public class CustomScpCfCloudPlatform extends ScpCfCloudPlatform {
    private static final String XSAPPNAME = "xsappname";
    private static final String SERVICE_CREDENTIALS = "credentials";
    private static final String SERVICE_NAME_XSUAA = "xsuaa";

    @Override
    public JsonObject getServiceCredentials(String serviceName, String servicePlan)
            throws CloudPlatformException, NoServiceBindingException, MultipleServiceBindingsException {

        if (serviceName.equals(com.sap.exciseduty.workaround.CustomScpCfCloudPlatform.SERVICE_NAME_XSUAA)) {
            return super.getServiceCredentials(serviceName, "application");
        }
        return super.getServiceCredentials(serviceName, servicePlan);
    }

}
