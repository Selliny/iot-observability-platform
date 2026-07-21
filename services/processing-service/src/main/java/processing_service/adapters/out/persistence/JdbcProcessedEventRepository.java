package processing_service.adapters.out.persistence;

import java.time.Instant;
import java.time.ZoneOffset;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import processing_service.application.port.out.ProcessedEventRepository;

@Repository
public class JdbcProcessedEventRepository implements ProcessedEventRepository {
    private final JdbcClient jdbcClient;

    public JdbcProcessedEventRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public boolean registerIfNotExists(String eventId, String topic, int partition, long offset, Instant processedAt) {
        int rowsAffected = jdbcClient.sql(
                "INSERT INTO processing.processed_event (event_id, topic_name, partition_number, kafka_offset, processed_at) "
                        + "VALUES (:eventId, :topicName, :partitionNumber, :kafkaOffset, :processedAt) ON CONFLICT (event_id) DO NOTHING")
                .param("eventId", eventId)
                .param("topicName", topic)
                .param("partitionNumber", partition)
                .param("kafkaOffset", offset)
                .param("processedAt", processedAt.atOffset(ZoneOffset.UTC))
                .update();
        return rowsAffected == 1;
    }

}
