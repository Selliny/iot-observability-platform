package ingestion_service.adapters.out.kafka;

import java.time.Instant;

import ingestion_service.domain.model.RejectedSensorMessage;

public record KafkaRejectedSensorMessageEvent(int schemaVersion, String failureId, String source, String topic,
        String reasonCode, String reason, String rawPayload, Instant failedAt) {
    public static KafkaRejectedSensorMessageEvent from(
            RejectedSensorMessage message) {
        return new KafkaRejectedSensorMessageEvent(
                1,
                message.failureId(),
                message.source(),
                message.topic(),
                message.reasonCode(),
                message.reason(),
                message.rawPayload(),
                message.failedAt());
    }
}
