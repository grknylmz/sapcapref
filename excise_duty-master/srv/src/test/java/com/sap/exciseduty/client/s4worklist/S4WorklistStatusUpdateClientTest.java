package com.sap.exciseduty.client.s4worklist;

import org.junit.Before;

import com.sap.exciseduty.client.exception.S4WorklistStatusUpdateFailedException;
import com.sap.exciseduty.client.s4worklist.IS4WorklistStatus;
import com.sap.exciseduty.client.s4worklist.S4WorklistStatusUpdateClient;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;

import java.util.ArrayList;

public class S4WorklistStatusUpdateClientTest {

    S4WorklistStatusUpdateClient cut;

    @Before
    public void init() {

        cut = new S4WorklistStatusUpdateClient();
    }

    public void testExecute() throws S4WorklistStatusUpdateFailedException {
        ArrayList<S4WorklistItem> list = new ArrayList<>();
        S4WorklistItem item = new S4WorklistItem();
        item.setMaterialDocumentNumber("4900007122");
        item.setMaterialYear("2017");
        item.setLine("0001");
        item.setBillOfMaterialItemNodeNumber("00000000");
        list.add(item);
        item = new S4WorklistItem();
        item.setMaterialDocumentNumber("4900007123");
        item.setMaterialYear("2017");
        item.setLine("0001");
        item.setBillOfMaterialItemNodeNumber("00000000");
        list.add(item);

        cut.updateWorklistItemStatus(list, IS4WorklistStatus.PENDING);

    }
}
