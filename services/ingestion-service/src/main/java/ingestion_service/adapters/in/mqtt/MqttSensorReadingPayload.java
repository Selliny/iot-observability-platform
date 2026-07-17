package ingestion_service.adapters.in.mqtt;

import java.math.BigDecimal;
import java.time.Instant;

import ingestion_service.domain.model.SensorType;

public record MqttSensorReadingPayload(int schemaVersion, String eventId, String tenantId, String plantId,
        String lineId, String machineId, String sensorId, SensorType type, BigDecimal value, String unit,
        long sequenceNumber, Instant occurredAt) {

}
