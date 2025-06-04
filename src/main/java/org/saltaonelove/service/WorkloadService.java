package org.saltaonelove.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.saltaonelove.dto.workload.WorkloadRequest;
import org.saltaonelove.util.auth.JwtUtil;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.saltaonelove.exception.GlobalExceptionHandler.log;

@Service
public class WorkloadService {
    private final RestTemplate restTemplate;
    private static final String WORKLOAD_URL = "http://gym-shift-calculation-service:8082/api/workload";

    public WorkloadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "recoverMethod")
    public Boolean updateTrainerWorkload(WorkloadRequest workloadRequest) {
        String token = JwtUtil.getJwtTokenFromContext();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TransactionId", MDC.get("transactionId"));
        headers.setBearerAuth(token);

        HttpEntity<WorkloadRequest> requestEntity = new HttpEntity<>(workloadRequest, headers);

        ResponseEntity<Void> addWorkload = restTemplate.postForEntity(
                WORKLOAD_URL, requestEntity, Void.class);

        return addWorkload.getStatusCode().is2xxSuccessful();
    }

    private Boolean recoverMethod(WorkloadRequest request, Throwable ex) {
        log.warn("Fallback triggered due to: {}", ex.toString());
        return false;
    }


}
