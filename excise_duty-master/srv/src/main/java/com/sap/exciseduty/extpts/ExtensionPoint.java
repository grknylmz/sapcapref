package com.sap.exciseduty.extpts;

public interface ExtensionPoint<Input, Output> {

    public Output call(Input input, Extension<Input, Output> defaultImplementation) throws Exception;

    default Output call(Input input) throws Exception {
        return call(input, null);
    }

    public String getExtensionPointName();

    @SuppressWarnings("serial")
    class ExecutionException extends RuntimeException {

        private final String extensionPointName;

        public ExecutionException(String extensionPointName, Throwable cause) {
            super("ExtensionPoint " + extensionPointName + " : " + getOriginalCause(cause).getMessage(), getOriginalCause(cause));
            this.extensionPointName = extensionPointName;
        }

        public ExecutionException(ExtensionPoint<?, ?> extensionPoint, Throwable cause) {
            this(extensionPoint.getExtensionPointName(), cause);
        }

        public String getExtensionPointName() {
            return extensionPointName;
        }

        private static Throwable getOriginalCause(Throwable cause) {
            if (cause instanceof ExecutionException) {
                return getOriginalCause(cause.getCause());
            }
            return cause;
        }

    }

}
