package com.sap.exciseduty.odata.extension;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;

import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.InitTransaction;
import com.sap.cloud.sdk.service.prov.api.exits.InitTransactionResponse;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.request.DeleteRequest;
import com.sap.cloud.sdk.service.prov.api.request.GenericRequest;
import com.sap.cloud.sdk.service.prov.api.request.UpdateRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.exciseduty.entities.IExciseDutyEntities;
import com.sap.exciseduty.entities.ITaxWarehouse;
import com.sap.exciseduty.utility.WebSecurityConfig;
import com.sap.xs2.security.container.SecurityContext;
import com.sap.xs2.security.container.UserInfo;
import com.sap.xs2.security.container.UserInfoException;

public class InitTransactionExtension {

    @InitTransaction(serviceNames = { IExciseDutyEntities.TAX_WAREHOUSE_SERVICE })
    public InitTransactionResponse initTransaction(List<GenericRequest> requests,
            ExtensionHelper eh) {

        for (GenericRequest genericRequest : requests) {
            if (genericRequest instanceof CreateRequest ||
                    genericRequest instanceof UpdateRequest ||
                    genericRequest instanceof DeleteRequest) {
                /**
                 * Checks are only needed for entities which are in general create enabled but restricted by scopes.
                 * In case the entity is explicitly set to CREATE false within the CDS, the framework will check thius
                 */
                if (genericRequest.getServiceName() == IExciseDutyEntities.TAX_WAREHOUSE_SERVICE && genericRequest.getEntityMetadata().getName() == ITaxWarehouse.NAME) {
                    return checkEditScope(Arrays.asList(WebSecurityConfig.CONFEDIT_SCOPE));
                }
            }
        }
        return InitTransactionResponse.setSuccess().response();
    }

    private InitTransactionResponse checkEditScope(List<String> scopes) {
        // Verify Scope of incoming Request
        try {
            UserInfo userInfo = SecurityContext.getUserInfo();
            for (String scope : scopes) {
                if (!userInfo.checkLocalScope(scope)) {
                    return InitTransactionResponse.setError((ErrorResponse.getBuilder().setMessage("Forbidden").setStatusCode(HttpStatus.SC_FORBIDDEN)).response());
                }
            }
            // All Scopes found, return success
            return InitTransactionResponse.setSuccess().response();

        } catch (UserInfoException e1) {
            return InitTransactionResponse.setError((ErrorResponse.getBuilder().setMessage("Forbidden").setStatusCode(HttpStatus.SC_FORBIDDEN)).response());
        }
    }

}
