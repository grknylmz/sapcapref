package com.sap.exciseduty.client.s4worklist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplex;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyprojectperf.ED_PERF_CDS;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultCustomTheExciseDutyProjectPerfService;
import com.sap.exciseduty.utility.VdmMapper;

public class WorklistCommandByKeyPerformance extends ErpCommand<ExciseDutyComplex> {
    private final Logger logger = LoggerFactory.getLogger(WorklistCommandByKeyPerformance.class);
    private final String materialDocumentNumber;
    private final String materialDocumentYear;
    private final String materialDocumentItem;
    private final String billOfMaterialItemNodeNumber;

    public WorklistCommandByKeyPerformance(final ErpConfigContext configContext, String materialDocumentNumber, String materialDocumentYear, String materialDocumentItem, String billOfMaterialItemNodeNumber) {
        super(WorklistCommandByKeyPerformance.class, configContext);
        this.materialDocumentNumber = materialDocumentNumber;
        this.materialDocumentYear = materialDocumentYear;
        this.materialDocumentItem = materialDocumentItem;
        this.billOfMaterialItemNodeNumber = billOfMaterialItemNodeNumber;
    }

    @Override
    protected ExciseDutyComplex run() throws Exception {

        ED_PERF_CDS item = new DefaultCustomTheExciseDutyProjectPerfService().getED_PERF_CDSByKey(materialDocumentNumber, materialDocumentYear, materialDocumentItem, billOfMaterialItemNodeNumber).execute(getConfigContext());
        return VdmMapper.mapExciseDutyComplex(item);
    }

}