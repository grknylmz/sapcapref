package com.sap.exciseduty.client.s4worklist;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplex;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplexFluentHelper;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultCustomTheExciseDutyProjectService;

public class WorklistCommand extends ErpCommand<List<ExciseDutyComplex>> {
    private final Logger logger = LoggerFactory.getLogger(WorklistCommand.class);
    private final int topCount;
    private final String IS4WorklistStatus;

    public WorklistCommand(final ErpConfigContext configContext, String is4WorklistStatus, int topCount) {
        super(getCommandSetter(WorklistCommand.class), configContext);
        this.topCount = topCount;
        this.IS4WorklistStatus = is4WorklistStatus;
    }

    @Override
    protected List<ExciseDutyComplex> run() throws Exception {

        List<ExciseDutyComplex> items = getDefaultService().execute(getConfigContext());
        logger.info("Retrieved {} items with status {} for the System", items.size(), IS4WorklistStatus);

        return items;
    }

    @Override
    protected List<ExciseDutyComplex> getFallback() {
        logger.warn("Fallback called because of exception:", getExecutionException());

        return Collections.emptyList();
    }

    private ExciseDutyComplexFluentHelper getDefaultService() {
        ExciseDutyComplexFluentHelper service = new DefaultCustomTheExciseDutyProjectService().getAllExciseDutyComplex();
        service.top(topCount);

        service.filter((ExciseDutyComplex.STATS).eq(IS4WorklistStatus));

        return service;
    }

    /*
     * this is required to enforce
     */
    private static Setter getCommandSetter(final Class<?> commandClass) {
        final String groupKey = HystrixUtil.getGlobalKey(commandClass);

        // use the level of isolation that is required here
        final String commandKey = HystrixUtil.getTenantIsolatedKey(commandClass);

        return Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(groupKey))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties
                                .Setter()
                                .withCoreSize(10)
                                .withQueueSizeRejectionThreshold(100)
                                .withMaxQueueSize(100))
                .andCommandPropertiesDefaults(HystrixUtil.getDefaultErpCommandProperties());
    }

}