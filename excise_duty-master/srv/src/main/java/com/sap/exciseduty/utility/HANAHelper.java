package com.sap.exciseduty.utility;

public final class HANAHelper {

    public static boolean convertBooleanValue(Object value) {

        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }

        if (value instanceof Number) {
            return ((Number) value).intValue() == 1 ? true : false;
        }

        return false;
    }
}
