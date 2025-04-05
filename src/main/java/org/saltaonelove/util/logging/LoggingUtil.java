package org.saltaonelove.util.logging;

import org.saltaonelove.util.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class LoggingUtil {

    private final Logger logger;

    private LoggingUtil(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public static LoggingUtil getLogger(Class<?> clazz) {
        return new LoggingUtil(clazz);
    }

    public void startTransaction() {
        String txId = UUID.randomUUID().toString();
        MDC.put("transactionId", txId);
        logger.info("🎬 Transaction started: {}", txId);
    }

    public void endTransaction() {
        logger.info("🔚 Transaction ended: {}", getTxId());
        MDC.remove("transactionId");
    }

    public String getTxId() {
        return MDC.get("transactionId");
    }

    public void logRestCall(ResponseEntity<?> response) {
        logger.info("✅ {} {} → {}",
                RequestContext.getMethod(),
                RequestContext.getEndpoint(),
                response.getStatusCode()
        );
    }
    public void logError(String method, String endpoint, Exception ex, Object body) {
        logger.error("❌ {} {} failed: {}\nError body: {}", method, endpoint, ex.getMessage(), body);
    }

    public void info(String message, Object... args) {
        logger.info("[{}] " + message, prependTxId(args));
    }

    public void error(String message, Object... args) {
        logger.error("[{}] " + message, prependTxId(args));
    }

    public void warn(String message, Object... args) {
        logger.warn("[{}] " + message, prependTxId(args));
    }

    private Object[] prependTxId(Object[] args) {
        Object[] newArgs = new Object[args.length + 1];
        newArgs[0] = getTxId();
        System.arraycopy(args, 0, newArgs, 1, args.length);
        return newArgs;
    }
}