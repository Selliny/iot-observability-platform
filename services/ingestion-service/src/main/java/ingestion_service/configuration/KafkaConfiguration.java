package ingestion_service.configuration;

import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import ingestion_service.application.port.out.KafkaSensorReadingEvent;

@Configuration
public class KafkaConfiguration {
    @Bean
    public ProducerFactory<String, KafkaSensorReadingEvent> sensorReadingProducerFactory(
            KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

    @Bean
    public KafkaTemplate<String, KafkaSensorReadingEvent> sensorReadingKafkaTemplate(
            ProducerFactory<String, KafkaSensorReadingEvent> sensorReadingProducerFactory) {
        return new KafkaTemplate<>(sensorReadingProducerFactory);
    }
}
