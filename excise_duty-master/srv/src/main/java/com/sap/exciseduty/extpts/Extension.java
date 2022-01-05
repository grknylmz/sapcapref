package com.sap.exciseduty.extpts;

@FunctionalInterface
public interface Extension<Input, Output> {

    Output call(Input input) throws Exception;

}
