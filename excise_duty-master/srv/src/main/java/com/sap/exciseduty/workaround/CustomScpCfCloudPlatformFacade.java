package com.sap.exciseduty.workaround;

import javax.annotation.Nonnull;

import com.sap.cloud.sdk.cloudplatform.CloudPlatform;
import com.sap.cloud.sdk.cloudplatform.ScpCfCloudPlatform;
import com.sap.cloud.sdk.cloudplatform.ScpCfCloudPlatformFacade;

public class CustomScpCfCloudPlatformFacade extends ScpCfCloudPlatformFacade {
    @Nonnull
    @Override
    public Class<? extends CloudPlatform> getCloudPlatformClass() {
        return ScpCfCloudPlatform.class;
    }

    @Nonnull
    @Override
    public ScpCfCloudPlatform getCloudPlatform() {
        return new CustomScpCfCloudPlatform();
    }

}
