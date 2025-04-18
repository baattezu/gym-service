package org.saltaonelove.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AuthMetrics {

    private final Counter totalLogins;
    private final AtomicInteger uniqueDailyLogins = new AtomicInteger();
    private final Set<Long> uniqueUserIdsToday = ConcurrentHashMap.newKeySet();

    public AuthMetrics(MeterRegistry meterRegistry) {
        this.totalLogins = meterRegistry.counter("gymcrm_login_success_total");
        meterRegistry.gauge("gymcrm_login_unique_today", uniqueDailyLogins);
    }

    public void onSuccessfulLogin(Long userId) {
        totalLogins.increment();
        if (uniqueUserIdsToday.add(userId)) {
            uniqueDailyLogins.incrementAndGet();
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyLogins() {
        uniqueUserIdsToday.clear();
        uniqueDailyLogins.set(0);
    }
}