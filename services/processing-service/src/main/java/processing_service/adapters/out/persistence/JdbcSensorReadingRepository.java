package processing_service.adapters.out.persistence;

import java.time.ZoneOffset;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import processing_service.application.port.out.SensorReadingRepository;
import processing_service.domain.model.SensorReading;

@Repository
public class JdbcSensorReadingRepository implements SensorReadingRepository {
    private final JdbcClient jdbcClient;

    public JdbcSensorReadingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(SensorReading reading) {
        jdbcClient.sql(
                "INSERT INTO processing.sensor_reading (event_id, tenant_id, plant_id, line_id, machine_id, sensor_id, sensor_type, value,unit, sequence_number, occurred_at, ingested_at, processed_at) VALUES ( :eventId, :tenantId, :plantId, :lineId, :machineId, :sensorId, :sensorType, :value, :unit, :sequenceNumber, :occurredAt, :ingestedAt, :processedAt)")
                .param("eventId", reading.eventId())
                .param("tenantId", reading.tenantId())
                .param("plantId", reading.plantId())
                .param("lineId", reading.lineId())
                .param("machineId", reading.machineId())
                .param("sensorId", reading.sensorId())
                .param("sensorType", reading.type().name())
                .param("value", reading.value())
                .param("unit", reading.unit())
                .param("sequenceNumber", reading.sequenceNumber())
                .param("occurredAt", reading.occurredAt().atOffset(ZoneOffset.UTC))
                .param("ingestedAt", reading.ingestedAt().atOffset(ZoneOffset.UTC))
                .param("processedAt", reading.processedAt().atOffset(ZoneOffset.UTC))
                .update();
    }
}
