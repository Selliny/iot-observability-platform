package processing_service.application.command;

import java.math.BigDecimal;
import java.time.Instant;

import processing_service.domain.model.SensorType;

public record ProcessSensorReadingCommand(String eventId, String tenantId, String plantId, String lineId,
        String machineId, String sensorId, SensorType type, BigDecimal value, String unit, long sequenceNumber,
        Instant occurredAt,
        Instant ingestedAt) {

}
