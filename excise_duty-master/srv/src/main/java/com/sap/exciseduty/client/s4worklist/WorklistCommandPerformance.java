package com.sap.exciseduty.client.s4worklist;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplex;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyprojectperf.ED_PERF_CDS;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyprojectperf.ED_PERF_CDSFluentHelper;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultCustomTheExciseDutyProjectPerfService;
import com.sap.exciseduty.utility.VdmMapper;

public class WorklistCommandPerformance extends ErpCommand<List<ExciseDutyComplex>> {
    private final Logger logger = LoggerFactory.getLogger(WorklistCommandPerformance.class);
    private final int topCount;
    private final String IS4WorklistStatus;

    public WorklistCommandPerformance(final ErpConfigContext configContext, String is4WorklistStatus, int topCount) {
        super(WorklistCommandPerformance.class, configContext);
        this.topCount = topCount;
        this.IS4WorklistStatus = is4WorklistStatus;
    }

    @Override
    protected List<ExciseDutyComplex> run() throws Exception {

        List<ExciseDutyComplex> items = getPerformanceService().execute(getConfigContext()).parallelStream().map(item -> VdmMapper.mapExciseDutyComplex(item)).collect(Collectors.toList());
        logger.info("Performance Landscape: Retrieved {} items with status {} for the System", items.size(), IS4WorklistStatus);

        return items;
    }

    @Override
    protected List<ExciseDutyComplex> getFallback() {
        logger.warn("Performance Landscape: Fallback called because of exception:", getExecutionException());

        return Collections.emptyList();
    }

    private ED_PERF_CDSFluentHelper getPerformanceService() {
        ED_PERF_CDSFluentHelper service = new DefaultCustomTheExciseDutyProjectPerfService().getAllED_PERF_CDS();

        service.filter((ED_PERF_CDS.STATS).eq(IS4WorklistStatus));

        service.top(topCount);

        return service;
    }

}