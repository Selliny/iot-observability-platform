package processing_service.adapters.in.kafka;

import java.math.BigDecimal;
import java.time.Instant;

public record KafkaSensorReadingEvent(int schemaVersion, String eventId, String tenantId, String plantId, String lineId,
        String machineId, String sensorId, String type, BigDecimal value, String unit, long sequenceNumber,
        Instant occurredAt, Instant ingestedAt) {

}
