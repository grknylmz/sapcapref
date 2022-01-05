package com.sap.exciseduty.odata.remote;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryResult;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.productmaster.Product;
import com.sap.cloud.sdk.service.prov.api.operations.Query;
import com.sap.cloud.sdk.service.prov.api.operations.Read;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.api.request.ReadRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.QueryResponse;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponse;

public class S4ApiProduct {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Query(serviceName = "MaterialMasterService", entity = "A_Product")
    public QueryResponse queryA_Product(QueryRequest qryRequest) {
        QueryResponse queryResponse = null;
        try

        {
            List<String> selectProperties = new ArrayList<String>();
            if (qryRequest.getSelectProperties() == null || qryRequest.getSelectProperties().isEmpty()) {
                selectProperties.add(Product.PRODUCT.getFieldName());
            } else {
                selectProperties = qryRequest.getSelectProperties();
            }

            ODataQueryBuilder queryBuilder = ODataQueryBuilder
                    .withEntity("/sap/opu/odata/sap/API_PRODUCT_SRV", "A_Product")
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

            // .orderBy -> Missing
            // . CustomerQuery -> Missing

            /* As per conversation in https://github.wdf.sap.corp/cap/issues/issues/411 this class is not supported yet and since we don't have the time to follow up on 
             * alternative approaches, I am commenting out this piece of code for now.
            // Filter
            if (qryRequest instanceof QueryRequestV2WtFilterImpl) {
                ExpressionHelper requestWithFilter = (ExpressionHelper) qryRequest;
                Expression filterExpression = requestWithFilter.getQueryExpression();

                V2FilterExpression v2FilterExpression = new V2FilterExpression();
                FilterExpression filterexpression = v2FilterExpression.Convert((ExpressionNode) filterExpression);
                queryBuilder.filter(filterexpression);
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
    @Read(serviceName = "MaterialMasterService", entity = "A_Product")
    public ReadResponse readA_Customer(ReadRequest readRequest) {
        return null;

    }

}
