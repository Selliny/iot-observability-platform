package processing_service.application.port.out;

import java.time.Instant;

public interface ProcessedEventRepository {
    boolean registerIfNotExists(String eventId, String topic, int partition, long offset, Instant processedAt);
}
