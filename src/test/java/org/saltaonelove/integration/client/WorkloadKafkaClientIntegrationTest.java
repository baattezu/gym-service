package org.saltaonelove.integration.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.saltaonelove.clients.workload.WorkloadKafkaClient;
import org.saltaonelove.gymshared.model.workload.ActionType;
import org.saltaonelove.gymshared.model.workload.WorkloadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@EmbeddedKafka(partitions = 1, topics = { "workload-topic" }, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"
})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DirtiesContext
public class WorkloadKafkaClientIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private WorkloadKafkaClient workloadKafkaClient;

    private Consumer<String, Object> consumer;

    @BeforeAll
    void setUp(@Autowired EmbeddedKafkaBroker broker) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", broker);
        consumer = new DefaultKafkaConsumerFactory<>(consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(Object.class, false)).createConsumer();

        broker.consumeFromAnEmbeddedTopic(consumer, "workload-topic");
    }

    @Test
    void testSendUpdateTrainerWorkload() {
        WorkloadRequest request = new WorkloadRequest(
                "John.Doe", "John", "Doe",
                true, LocalDate.of(2025,07,01),
                50L, ActionType.ADD);

        workloadKafkaClient.updateTrainerWorkload(request);

        ConsumerRecord<String, Object> record = KafkaTestUtils.getSingleRecord(consumer, "workload-topic", Duration.ofMillis(5000));
        assertNotNull(record);

        Object messageMap = record.value();
        WorkloadRequest actual = objectMapper.convertValue(messageMap, WorkloadRequest.class);
        assertEquals(request, actual);

        Headers headers = record.headers();
        assertNotNull(headers.lastHeader("commandType"));
        assertEquals("UPDATE_WORKLOAD", new String(headers.lastHeader("commandType").value(), StandardCharsets.UTF_8));
    }
}