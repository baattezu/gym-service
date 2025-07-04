package org.saltaonelove.clients.workload;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.saltaonelove.gymshared.kafka.dto.CommandType;
import org.saltaonelove.gymshared.model.workload.WorkloadRequest;
import org.slf4j.MDC;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Primary
@Slf4j
public class WorkloadKafkaClient implements WorkloadClient {

    private static final String TOPIC = "workload-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public WorkloadKafkaClient(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void updateTrainerWorkload(WorkloadRequest workloadRequest) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, workloadRequest);
        record.headers().add("commandType", CommandType.UPDATE_WORKLOAD.asHeader());
        String traceId = MDC.get("transactionId");
        if (traceId != null) {
            record.headers().add("traceId", traceId.getBytes(StandardCharsets.UTF_8));
        }
        try {
            kafkaTemplate.send(record).get();
        } catch (Exception e) {
            log.error("Kafka send failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTrainerWorkloadHistory(String username) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, username);
        record.headers().add("commandType", CommandType.DELETE_HISTORY.asHeader());
        String traceId = MDC.get("transactionId");
        if (traceId != null) {
            record.headers().add("traceId", traceId.getBytes(StandardCharsets.UTF_8));
        }
        kafkaTemplate.send(record);
    }

}
