package ingestion_service.domain.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import ingestion_service.domain.exception.InvalidSensorReadingException;

//esta lendo um dado imutavel
public record SensorReading(String eventId,
        String tenantId,
        String plantId,
        String lineId,
        String machineId,
        String sensorId,
        SensorType type,
        BigDecimal value,
        String unit,
        Long sequenceNumber,
        Instant occurredAt,
        Instant ingestionAt) {
    private static final Duration MaximumFutureTolerance = Duration.ofMinutes(5);

    public SensorReading {
        requiredText(eventId, "eventId");
        requiredText(plantId, "plantId");
        requiredText(lineId, "lineId");
        requiredText(machineId, "machineId");
        requiredText(sensorId, "sensorId");

        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(occurredAt, "occurredAt cannot be null");
        Objects.requireNonNull(ingestionAt, "ingestionAt cannot be null");
        // occurredAt should not be too far in the future compared to ingestionAt
        if (occurredAt.isAfter(ingestionAt.plus(MaximumFutureTolerance))) {
            throw new InvalidSensorReadingException(
                    "occurredAt cannot be more than " + MaximumFutureTolerance.toMinutes() + " minutes in the future");
        }
    }

    public static void requiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidSensorReadingException(fieldName + " is required");
        }
    }
}
