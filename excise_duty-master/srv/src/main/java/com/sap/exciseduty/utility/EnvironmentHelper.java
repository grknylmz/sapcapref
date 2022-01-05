package com.sap.exciseduty.utility;

public class EnvironmentHelper {

    public static String getVariableWithDefault(String environmentVariable, String fallback) {
        return (String) getVariableWithDefaultObject(environmentVariable, fallback);
    }

    protected static Object getVariableWithDefaultObject(String environmentVariable, Object fallback) {
        String value = System.getenv(environmentVariable);
        if (value != null) {
            return value;
        }
        return fallback;
    }
}
