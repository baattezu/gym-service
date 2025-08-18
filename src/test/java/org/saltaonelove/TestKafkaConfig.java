package org.saltaonelove;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
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