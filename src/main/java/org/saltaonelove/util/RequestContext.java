package org.saltaonelove.util;

public class RequestContext {
    private static final ThreadLocal<String> endpointHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> methodHolder = new ThreadLocal<>();

    public static void set(String method, String endpoint) {
        methodHolder.set(method);
        endpointHolder.set(endpoint);
    }

    public static String getMethod() {
        return methodHolder.get();
    }

    public static String getEndpoint() {
        return endpointHolder.get();
    }

    public static void clear() {
        methodHolder.remove();
        endpointHolder.remove();
    }
}