package ingestion_service.application.command;

import java.math.BigDecimal;
import java.time.Instant;

import ingestion_service.domain.model.SensorType;

public record IngestSensorReadingCommand(String eventId,
        String tenantId,
        String plantId,
        String lineId,
        String machineId,
        String sensorId,
        SensorType type,
        BigDecimal value,
        String unit,
        long sequenceNumber,
        Instant occurredAt,
        Instant ingestionAt) {

}
