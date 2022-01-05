package com.sap.exciseduty.workaround;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sap.cloud.sdk.cloudplatform.CloudPlatformAccessor;

public class CustomServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        CloudPlatformAccessor.setCloudPlatformFacade(new CustomScpCfCloudPlatformFacade());
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        // nothing to do
    }
}
