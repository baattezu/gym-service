package org.saltaonelove;

import jakarta.inject.Qualifier;
import org.mockito.Mockito;
import org.saltaonelove.clients.workload.WorkloadClient;
import org.saltaonelove.clients.workload.WorkloadKafkaClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@TestConfiguration
public class TestKafkaConfig {

    @Bean(name = "kafkaTestMockTemplate")
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return Mockito.mock(KafkaTemplate.class);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return Mockito.mock(ProducerFactory.class);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return Mockito.mock(ConsumerFactory.class);
    }


}