package processing_service.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

@SpringBootTest
class JdbcProcessedEventRepositoryTest {

    private static final String EVENT_ID = "repo-test-processed-event";

    @Autowired
    private JdbcProcessedEventRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @AfterEach
    void cleanUp() {
        jdbcClient.sql("DELETE FROM processing.processed_event WHERE event_id = :eventId")
                .param("eventId", EVENT_ID)
                .update();
    }

    @Test
    void shouldRegisterEventOnFirstAttempt() {
        boolean registered = repository.registerIfNotExists(
                EVENT_ID, "iot.sensor-readings.v1", 0, 42L, Instant.parse("2026-07-21T12:00:00Z"));

        assertThat(registered).isTrue();

        Long count = jdbcClient.sql("SELECT count(*) FROM processing.processed_event WHERE event_id = :eventId")
                .param("eventId", EVENT_ID)
                .query(Long.class)
                .single();
        assertThat(count).isEqualTo(1L);
    }

    @Test
    void shouldNotRegisterTheSameEventTwice() {
        boolean firstAttempt = repository.registerIfNotExists(
                EVENT_ID, "iot.sensor-readings.v1", 0, 42L, Instant.parse("2026-07-21T12:00:00Z"));
        boolean secondAttempt = repository.registerIfNotExists(
                EVENT_ID, "iot.sensor-readings.v1", 0, 43L, Instant.parse("2026-07-21T12:00:05Z"));

        assertThat(firstAttempt).isTrue();
        assertThat(secondAttempt).isFalse();

        Long count = jdbcClient.sql("SELECT count(*) FROM processing.processed_event WHERE event_id = :eventId")
                .param("eventId", EVENT_ID)
                .query(Long.class)
                .single();
        assertThat(count).isEqualTo(1L);
    }
}
