package com.sap.exciseduty.utility;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface GetAsJSONObject {

    default JsonElement getAsJsonObject() {
        Gson gson = new Gson();
        return gson.toJsonTree(this);
    }

}
