package domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record SensorReading(String eventId, String tenantId, String plantId, String lineId, String machineId,
        String sensorId, SensorType type, BigDecimal value, String unit, long sequenceNumber, Instant occurredAt) {
    public SensorReading {
        requireText(eventId, "eventId");
        requireText(tenantId, "tenantId");
        requireText(plantId, "plantId");
        requireText(lineId, "lineId");
        requireText(machineId, "machineId");
        requireText(sensorId, "sensorId");
        requireText(unit, "unit");

        Objects.requireNonNull(type, "type is required");
        Objects.requireNonNull(value, "value is required");
        Objects.requireNonNull(
                occurredAt,
                "occurredAt is required");

        if (sequenceNumber < 0) {
            throw new IllegalArgumentException(
                    "sequenceNumber cannot be negative");
        }
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }
}

// Por que o simulador também possui um modelo de domínio?
// Porque ele possui regras próprias:
// --> uma leitura precisa possuir identificação;
// --> precisa ter valor;
// --> precisa ter timestamp;
// --> a sequência não pode ser negativa;
// --> depois poderá simular estados e falhas.