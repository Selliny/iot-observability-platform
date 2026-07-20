package processing_service.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

import processing_service.domain.exception.InvalidSensorReadingException;

public record SensorReading(String eventId, String tenantId, String plantId, String lineId, String machineId,
        String sensorId, SensorType type, BigDecimal value, String unit, long sequenceNumber, Instant occurredAt,
        Instant ingestedAt, Instant processedAt) {
    public SensorReading {
        requireText(eventId, "eventId");
        requireText(tenantId, "tenantId");
        requireText(plantId, "plantId");
        requireText(lineId, "lineId");
        requireText(machineId, "machineId");
        requireText(sensorId, "sensorId");

        if (type == null) {
            throw new InvalidSensorReadingException("type is required");
        }

        if (value == null) {
            throw new InvalidSensorReadingException("value is required");
        }

        if (occurredAt == null) {
            throw new InvalidSensorReadingException("occurredAt is required");
        }

        if (ingestedAt == null) {
            throw new InvalidSensorReadingException("ingestedAt is required");
        }

        if (processedAt == null) {
            throw new InvalidSensorReadingException("processedAt is required");
        }

        if (sequenceNumber < 0) {
            throw new InvalidSensorReadingException("sequenceNumber cannot be negative");
        }
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidSensorReadingException(fieldName + " is required");
        }
    }
}
