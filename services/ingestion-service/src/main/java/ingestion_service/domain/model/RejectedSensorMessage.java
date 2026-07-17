package ingestion_service.domain.model;

import java.time.Instant;

public record RejectedSensorMessage(String failureId, String source, String topic, String reasonCode, String reason,
        String rawPayload, Instant failedAt) {

}
