package ingestion_service.application.port.out;

import java.math.BigDecimal;
import java.time.Instant;

import ingestion_service.domain.model.SensorReading;

public record KafkaSensorReadingEvent(int schemaVersion,
        String eventId,
        String tenantId,
        String plantId,
        String lineId,
        String machineId,
        String sensorId,
        String type,
        BigDecimal value,
        String unit,
        long sequenceNumber,
        Instant occurredAt,
        Instant ingestedAt) {
    public static KafkaSensorReadingEvent from(SensorReading reading) {
        return new KafkaSensorReadingEvent(1, reading.eventId(), reading.tenantId(), reading.plantId(),
                reading.lineId(), reading.machineId(), reading.sensorId(), reading.type().name(), reading.value(),
                reading.unit(), reading.sequenceNumber(), reading.occurredAt(), reading.ingestionAt());
    }
}
