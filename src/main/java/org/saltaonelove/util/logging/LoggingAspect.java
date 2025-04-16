package org.saltaonelove.util.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final LoggingUtil log = LoggingUtil.getLogger(LoggingAspect.class);

    @Around("@annotation(org.saltaonelove.util.logging.annotation.LogRestCall) || @within(org.saltaonelove.util.logging.annotation.LogRestCall)")
    public Object logRest(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }

        if (result instanceof ResponseEntity<?> response) {
            log.logRestCall(response);
        } else {
            log.warn("⚠️ Unexpected return type in @LogRestCall method: {}", joinPoint.getSignature());
        }
        return result;
    }

    @Around("@annotation(org.saltaonelove.util.logging.annotation.TransactionalWithLogging)")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        log.startTransaction();
        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            log.warn("⚠️ Exception in method: {}; message: {} ", joinPoint.getSignature().toShortString(),  ex.getMessage());
            throw ex;
        } finally {
            log.endTransaction();
        }
    }
}
