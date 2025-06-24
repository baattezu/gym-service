package org.saltaonelove.clients.workload;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.saltaonelove.dto.workload.WorkloadRequest;
import org.saltaonelove.gymshared.util.auth.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WorkloadRestClient implements WorkloadClient {
    private final RestTemplate restTemplate;
    private static final String WORKLOAD_URL = "http://gym-shift-calculation-service:8082/api/workload";

    private static final Logger log = LoggerFactory.getLogger(WorkloadRestClient.class);

    public WorkloadRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "recoverMethodForUpdatingWorkload")
    public void updateTrainerWorkload(WorkloadRequest workloadRequest) {
        String token = JwtUtil.getJwtTokenFromContext();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TransactionId", MDC.get("transactionId"));
        headers.setBearerAuth(token);

        HttpEntity<WorkloadRequest> requestEntity = new HttpEntity<>(workloadRequest, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                WORKLOAD_URL, requestEntity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()){
            log.warn(response.getBody().toString());
        }
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "recoverMethodForDeletionHistory")
    public void deleteTrainerWorkloadHistory(String username) {
        String token = JwtUtil.getJwtTokenFromContext();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TransactionId", MDC.get("transactionId"));
        headers.setBearerAuth(token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                WORKLOAD_URL + "/" + username, HttpMethod.DELETE, requestEntity, Void.class
        );

        if (!response.getStatusCode().is2xxSuccessful()){
            log.warn(response.getBody().toString());
        }
    }

    private void recoverMethodForUpdatingWorkload(WorkloadRequest request, Throwable ex) {
        log.warn("Fallback triggered due to: {}", ex.toString());
    }
    private void recoverMethodForDeletionHistory(String username, Throwable ex) {
        log.warn("Fallback triggered due to: {}", ex.toString());
    }


}
