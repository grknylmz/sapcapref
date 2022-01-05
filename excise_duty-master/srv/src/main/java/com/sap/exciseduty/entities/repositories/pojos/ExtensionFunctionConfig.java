package com.sap.exciseduty.entities.repositories.pojos;

public class ExtensionFunctionConfig {

    private String exciseDutyTypeId;
    private String extensionPoint;
    private String extensionDestination;

    public String getExciseDutyTypeId() {
        return exciseDutyTypeId;
    }

    public void setExciseDutyTypeId(String exciseDutyTypeId) {
        this.exciseDutyTypeId = exciseDutyTypeId;
    }

    public String getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(String extensionPoint) {
        this.extensionPoint = extensionPoint;
    }

    public String getExtensionDestination() {
        return extensionDestination;
    }

    public void setExtensionDestination(String extensionDestination) {
        this.extensionDestination = extensionDestination;
    }

}
