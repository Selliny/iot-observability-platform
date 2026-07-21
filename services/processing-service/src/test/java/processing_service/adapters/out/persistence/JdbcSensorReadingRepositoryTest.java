package processing_service.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import processing_service.domain.model.SensorReading;
import processing_service.domain.model.SensorType;

@SpringBootTest
class JdbcSensorReadingRepositoryTest {

    private static final String EVENT_ID = "repo-test-sensor-reading";

    @Autowired
    private JdbcSensorReadingRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @AfterEach
    void cleanUp() {
        jdbcClient.sql("DELETE FROM processing.sensor_reading WHERE event_id = :eventId")
                .param("eventId", EVENT_ID)
                .update();
    }

    @Test
    void shouldPersistReadingWithAllFieldsPreserved() {
        Instant occurredAt = Instant.parse("2026-07-21T12:00:00Z");
        Instant ingestedAt = Instant.parse("2026-07-21T12:00:01Z");
        Instant processedAt = Instant.parse("2026-07-21T12:00:02Z");

        SensorReading reading = new SensorReading(
                EVENT_ID,
                "tenant-01",
                "plant-01",
                "line-01",
                "machine-01",
                "sensor-01",
                SensorType.TEMPERATURE,
                BigDecimal.valueOf(55.5),
                "Celsius",
                1L,
                occurredAt,
                ingestedAt,
                processedAt);

        repository.save(reading);

        var row = jdbcClient.sql(
                "SELECT tenant_id, occurred_at, ingested_at, processed_at FROM processing.sensor_reading WHERE event_id = :eventId")
                .param("eventId", EVENT_ID)
                .query((rs, rowNum) -> new Object[] {
                        rs.getString("tenant_id"),
                        rs.getTimestamp("occurred_at").toInstant(),
                        rs.getTimestamp("ingested_at").toInstant(),
                        rs.getTimestamp("processed_at").toInstant() })
                .single();

        assertThat(row[0]).isEqualTo("tenant-01");
        assertThat(row[1]).isEqualTo(occurredAt);
        assertThat(row[2]).isEqualTo(ingestedAt);
        assertThat(row[3]).isEqualTo(processedAt);
    }
}
