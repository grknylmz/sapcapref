package com.sap.exciseduty.odata.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryResult;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.Customer;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import com.sap.cloud.sdk.service.prov.api.operations.Query;
import com.sap.cloud.sdk.service.prov.api.operations.Read;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.api.request.ReadRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.QueryResponse;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponse;

public class S4ApiBusinessPartner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Query(serviceName = "ShipToMasterService", entity = "A_Customer")
    public QueryResponse queryA_Customer(QueryRequest qryRequest) {
        QueryResponse queryResponse = null;
        try

        {
            List<String> selectProperties = new ArrayList<String>();
            if (qryRequest.getSelectProperties() == null || qryRequest.getSelectProperties().isEmpty()) {
                selectProperties.add(Customer.CUSTOMER.getFieldName());
                selectProperties.add(Customer.CUSTOMER_NAME.getFieldName());
            } else {
                selectProperties = qryRequest.getSelectProperties();
            }

            ODataQueryBuilder queryBuilder = ODataQueryBuilder
                    .withEntity("/sap/opu/odata/sap/API_BUSINESS_PARTNER", "A_Customer")
                    .enableMetadataCache()
                    .select(qryRequest.getSelectProperties());

            if (qryRequest.getTopOptionValue() > -1) {
                queryBuilder.top(qryRequest.getTopOptionValue());
            } else {
                queryBuilder.top(25);
            }
            if (qryRequest.getSkipOptionValue() > -1) {
                queryBuilder.skip(qryRequest.getSkipOptionValue());
            }

            // orderBy -> Missing

            // CustomerQuery
            Map<String, String> customQueryOptions = qryRequest.getCustomQueryOptions();
            if (customQueryOptions != null) {
                customQueryOptions.forEach((k, v) -> queryBuilder.param(k, v));
            }

            // Filter

            
            /* As per conversation in https://github.wdf.sap.corp/cap/issues/issues/411 this class is not supported yet and since we don't have the time to follow up on 
             * alternative approaches, I am commenting out this piece of code for now.         
            if (qryRequest instanceof QueryRequestV2WtFilterImpl) {
                ExpressionHelper requestWithFilter = (ExpressionHelper) qryRequest;
                Expression filterExpression = requestWithFilter.getQueryExpression();

                V2FilterExpression v2FilterExpression = new V2FilterExpression();
                FilterExpression filterexpression = v2FilterExpression.Convert((ExpressionNode) filterExpression);
                if (filterexpression != null) {
                    queryBuilder.filter(filterexpression);
                }
            }*/

            ODataQueryResult result = queryBuilder.build()
                    .execute("ExciseDuty-S4-onPremise");

            queryResponse = QueryResponse.setSuccess().setData(result.asListOfMaps()).response();
            return queryResponse;
        } catch (Exception e) {
            logger.error("!!Some error occurred while calling API_BUSINESS_PARTNER/A_Customer service!!", e);
            ErrorResponse er = ErrorResponse.getBuilder()
                    .setMessage("Read error occurred for API_BUSINESS_PARTNER/A_Customer " + e.getMessage())
                    .setStatusCode(500).setCause(e).response();
            queryResponse = QueryResponse.setError(er);
            return queryResponse;
        }

    }

    // Read A_Customer
    @Read(serviceName = "ShipToMasterService", entity = "A_Customer")
    public ReadResponse readA_Customer(ReadRequest readRequest) {
        ReadResponse readResponse = null;
        ODataQueryResult result;
        try {
            result = new DefaultBusinessPartnerService().getAllCustomer().toQuery().execute("ExciseDuty-S4-onPremise");

            List<Map<String, Object>> customerTypeMap = result.asListOfMaps();
            readResponse = ReadResponse.setSuccess().setData(customerTypeMap).response();

            return readResponse;
        } catch (ODataException e) {
            return null;
        }

    }

}
