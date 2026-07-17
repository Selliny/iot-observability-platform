package ingestion_service.application.port.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import ingestion_service.adapters.out.kafka.KafkaRejectedSensorMessageEvent;
import ingestion_service.domain.model.RejectedSensorMessage;

@Component
public class KafkaRejectedSensorMessagePublisher implements RejectedSensorMessagePublisher {
    private final KafkaTemplate<String, KafkaRejectedSensorMessageEvent> kafkaTemplate;
    private final String topicName;

    public KafkaRejectedSensorMessagePublisher(KafkaTemplate<String, KafkaRejectedSensorMessageEvent> kafkaTemplate,
            @Value("${app.kafka.topics.rejected-sensor-messages.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void publish(RejectedSensorMessage rejectedSensorMessage) {
        KafkaRejectedSensorMessageEvent event = KafkaRejectedSensorMessageEvent.from(rejectedSensorMessage);
        kafkaTemplate.send(topicName, rejectedSensorMessage.failureId(), event);
    }
}
