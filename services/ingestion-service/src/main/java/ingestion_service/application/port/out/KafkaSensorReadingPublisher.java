package ingestion_service.application.port.out;

import org.springframework.stereotype.Component;

import ingestion_service.domain.model.SensorReading;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

@Component
public class KafkaSensorReadingPublisher implements SensorReadingPublisher {
    private final KafkaTemplate<String, KafkaSensorReadingEvent> kafkaTemplate;
    private final String topicName;

    public KafkaSensorReadingPublisher(KafkaTemplate<String, KafkaSensorReadingEvent> kafkaTemplate,
            @Value("${app.kafka.topics.sensor-readings}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void publish(SensorReading sensorReading) {
        KafkaSensorReadingEvent event = KafkaSensorReadingEvent.from(sensorReading);
        String messageKey = buildMessageKey(sensorReading);
        kafkaTemplate.send(topicName, messageKey, event);
    }

    private String buildMessageKey(SensorReading reading) {
        return reading.tenantId() + ":" + reading.machineId();
    }

}
