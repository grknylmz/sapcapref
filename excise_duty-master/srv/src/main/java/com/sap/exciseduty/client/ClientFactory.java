package com.sap.exciseduty.client;

import com.rabbitmq.client.Channel;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.exciseduty.client.rabbitmq.WorklistEventConsumer;
import com.sap.exciseduty.client.rabbitmq.WorklistEventConsumerLocal;
import com.sap.exciseduty.client.rabbitmq.WorklistEventOrchestrator;
import com.sap.exciseduty.client.rabbitmq.WorklistEventOrchestratorLocal;
import com.sap.exciseduty.client.s4worklist.WorklistCommand;
import com.sap.exciseduty.client.s4worklist.WorklistCommandByKey;
import com.sap.exciseduty.client.s4worklist.WorklistCommandByKeyPerformance;
import com.sap.exciseduty.client.s4worklist.WorklistCommandPerformance;

public final class ClientFactory {

    private static final boolean IS_CLOUD_FOUNDRY_ENVIRONMENT = System.getenv("VCAP_APPLICATION") != null;
    private static final boolean IS_PERFORMANCE_TEST = isPerformanceEnvironment();

    private static final ClientFactory INSTANTIATION_DEFENDER = new ClientFactory();

    private ClientFactory() {
        /* prevent improper instantiation */
    }

    public static ErpCommand getWorklistItemCommand(ErpConfigContext configContext, String is4WorklistStatus, int topCount) {
        return IS_PERFORMANCE_TEST ? new WorklistCommandPerformance(configContext, is4WorklistStatus, topCount) : new WorklistCommand(configContext, is4WorklistStatus, topCount);
    }

    public static ErpCommand getWorklistItemByKeyCommand(ErpConfigContext configContext, String materialDocumentNumber, String materialDocumentYear, String materialDocumentItem, String billOfMaterialItemNodeNumber) {
        return IS_PERFORMANCE_TEST ? new WorklistCommandByKeyPerformance(configContext, materialDocumentNumber, materialDocumentYear, materialDocumentItem, billOfMaterialItemNodeNumber)
                : new WorklistCommandByKey(configContext, materialDocumentNumber, materialDocumentYear, materialDocumentItem, billOfMaterialItemNodeNumber);
    }

    public static WorklistEventOrchestrator getWorklistEventOrchestrator() {
        return IS_CLOUD_FOUNDRY_ENVIRONMENT ? WorklistEventOrchestrator.getInstance(INSTANTIATION_DEFENDER) : WorklistEventOrchestratorLocal.getInstance(INSTANTIATION_DEFENDER);
    }

    public static WorklistEventConsumer getWorklistEventConsumer(Channel channel) {
        return IS_CLOUD_FOUNDRY_ENVIRONMENT ? new WorklistEventConsumer(INSTANTIATION_DEFENDER, channel) : new WorklistEventConsumerLocal(INSTANTIATION_DEFENDER, channel);
    }

    private static boolean isPerformanceEnvironment() {
        return (System.getenv("IS_PERFORMANCE") == null) ? false : true;
    }
}
