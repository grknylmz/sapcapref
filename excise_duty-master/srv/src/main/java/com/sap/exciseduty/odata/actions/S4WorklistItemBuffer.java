package com.sap.exciseduty.odata.actions;

import java.util.HashMap;

import com.sap.exciseduty.client.rabbitmq.WorklistEventConsumer;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;

/**
 * 
 * This is a helper class to optimize performance. It allows to (temporarily)
 * store and retrieve S4WorklistItems. Currently used by
 * {@link WorklistEventConsumer} to pass incoming WorklistItem instances to
 * {@link ProcessSingleWorklistAction} in the actual worker thread for
 * processing.
 *
 */
public final class S4WorklistItemBuffer {

    private static final HashMap<String, S4WorklistItem> buffer = new HashMap<>();

    private S4WorklistItemBuffer() {
        /** prevent improper instantiation **/
    };

    public static void put(S4WorklistItem worklistItem) {
        String key = getKey(worklistItem.getMaterialYear(), worklistItem.getMaterialDocumentNumber(), worklistItem.getLine());
        buffer.put(key, worklistItem);
    }

    public static S4WorklistItem get(String materialDocumentYear, String materialDocumentNumber, String materialDocumentItem) {
        String key = getKey(materialDocumentYear, materialDocumentNumber, materialDocumentItem);
        S4WorklistItem result = buffer.get(key);
        buffer.remove(key);
        return result;
    }

    public static void clear() {
        buffer.clear();
    }

    private static String getKey(String materialDocumentYear, String materialDocumentNumber, String materialDocumentItem) {
        return String.join(".", materialDocumentYear, materialDocumentNumber, materialDocumentItem);
    }
}
