package com.sap.exciseduty.client.s4worklist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplex;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplexByKeyFluentHelper;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultCustomTheExciseDutyProjectService;

public class WorklistCommandByKey extends ErpCommand<ExciseDutyComplex> {
    private final Logger logger = LoggerFactory.getLogger(WorklistCommandByKey.class);
    private final String materialDocumentNumber;
    private final String materialDocumentYear;
    private final String materialDocumentItem;
    private final String billOfMaterialItemNodeNumber;

    public WorklistCommandByKey(final ErpConfigContext configContext, String materialDocumentNumber, String materialDocumentYear, String materialDocumentItem, String billOfMaterialItemNodeNumber) {
        super(WorklistCommandByKey.class, configContext);
        this.materialDocumentNumber = materialDocumentNumber;
        this.materialDocumentYear = materialDocumentYear;
        this.materialDocumentItem = materialDocumentItem;
        this.billOfMaterialItemNodeNumber = billOfMaterialItemNodeNumber;
    }

    @Override
    protected ExciseDutyComplex run() throws Exception {

        return getServiceByKey().execute(getConfigContext());
    }

    private ExciseDutyComplexByKeyFluentHelper getServiceByKey() {
        ExciseDutyComplexByKeyFluentHelper service = new DefaultCustomTheExciseDutyProjectService().getExciseDutyComplexByKey(materialDocumentNumber, materialDocumentYear, materialDocumentItem, billOfMaterialItemNodeNumber);

        return service;
    }

}