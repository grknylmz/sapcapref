package com.sap.exciseduty.odata.actions;

import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistStatusItem;

/**
 * 
 * Implements a global static instance of S4WorklistStatusItem. This instance is
 * globally visible. Each thread has its own instance.
 *
 */
public class ThreadLocalS4WorklistStatusItem {

    private static final ThreadLocal<S4WorklistStatusItem> instanceHolder = new ThreadLocal<>();

    public static S4WorklistStatusItem get() {
        return instanceHolder.get();
    }

    public static void set(S4WorklistStatusItem instance) {
        instanceHolder.set(instance);
    }

    public static void remove() {
        instanceHolder.remove();
    }
}
