package org.saltaonelove.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;
    private final Gauge dbHealthGauge;

    private volatile double dbStatus = 1.0;

    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate, MeterRegistry registry) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbHealthGauge = Gauge.builder("gymcrm_db_health_status", () -> dbStatus)
                .description("Database connectivity status: 1 = UP, 0 = DOWN")
                .register(registry);
    }

    @Override
    public Health health() {
        try {
            jdbcTemplate.execute("SELECT 1");
            dbStatus = 1.0;
            return Health.up().build();
        } catch (Exception e) {
            dbStatus = 0.0;
            return Health.down(e).build();
        }
    }
}