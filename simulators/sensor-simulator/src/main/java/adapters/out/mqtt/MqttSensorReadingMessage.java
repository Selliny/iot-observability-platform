package adapters.out.mqtt;

import java.math.BigDecimal;
import java.time.Instant;

import domain.model.SensorReading;

public record MqttSensorReadingMessage(int schemaVersion, String eventId, String tenantId, String plantId,
        String lineId, String machineId, String sensorId, String type, BigDecimal value, String unit,
        long sequenceNumber, Instant occurredAt) {
    public static MqttSensorReadingMessage from(
            SensorReading reading) {
        return new MqttSensorReadingMessage(
                1,
                reading.eventId(),
                reading.tenantId(),
                reading.plantId(),
                reading.lineId(),
                reading.machineId(),
                reading.sensorId(),
                reading.type().name(),
                reading.value(),
                reading.unit(),
                reading.sequenceNumber(),
                reading.occurredAt());
    }
}
