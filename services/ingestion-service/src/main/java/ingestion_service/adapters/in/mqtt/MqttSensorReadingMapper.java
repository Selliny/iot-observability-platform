package ingestion_service.adapters.in.mqtt;

import org.springframework.stereotype.Component;

import ingestion_service.application.command.IngestSensorReadingCommand;

@Component
public class MqttSensorReadingMapper {

    public IngestSensorReadingCommand toCommand(MqttSensorReadingPayload payload) {
        if (payload.schemaVersion() != 1) {
            throw new UnsupportedMqttSchemaVersionException(
                    "Unsupported schema version: "
                            + payload.schemaVersion());
        }

        requireText(payload.eventId(), "eventId");
        requireText(payload.tenantId(), "tenantId");
        requireText(payload.plantId(), "plantId");
        requireText(payload.lineId(), "lineId");
        requireText(payload.machineId(), "machineId");
        requireText(payload.sensorId(), "sensorId");
        requireText(payload.unit(), "unit");

        if (payload.type() == null) {
            throw new InvalidContractException("type is required");
        }
        if (payload.value() == null) {
            throw new InvalidContractException("value is required");
        }
        if (payload.occurredAt() == null) {
            throw new InvalidContractException("occurredAt is required");
        }

        return new IngestSensorReadingCommand(
                payload.eventId(),
                payload.tenantId(),
                payload.plantId(),
                payload.lineId(),
                payload.machineId(),
                payload.sensorId(),
                payload.type(),
                payload.value(),
                payload.unit(),
                payload.sequenceNumber(),
                payload.occurredAt(), null);
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidContractException(fieldName + " is required");
        }
    }
}
