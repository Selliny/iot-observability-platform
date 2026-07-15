package ingestion_service.adapters.in;

import java.math.BigDecimal;
import java.time.Instant;

import ingestion_service.application.command.IngestSensorReadingCommand;
import ingestion_service.domain.model.SensorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SensorReadingRequest(@NotBlank String eventId, @NotBlank String tenantId, @NotBlank String plantId,
        @NotBlank String lineId, @NotBlank String machineId, @NotBlank String sensorId,
        @NotNull SensorType type, @NotNull BigDecimal value, String unit, @PositiveOrZero long sequenceNumber,
        @NotNull Instant occurredAt) {
    public IngestSensorReadingCommand toCommand() {
        return new IngestSensorReadingCommand(eventId, tenantId, plantId, lineId, machineId, sensorId, type, value,
                unit, sequenceNumber, occurredAt, occurredAt);
    }
}
