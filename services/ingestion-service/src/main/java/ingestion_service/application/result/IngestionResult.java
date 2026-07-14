package ingestion_service.application.result;

import java.time.Instant;

public record IngestionResult(String eventId, String status, Instant ingestionAt) {
    public static IngestionResult accepted(String eventId, Instant ingestionAt) {
        return new IngestionResult(eventId, "ACCEPTED", ingestionAt);
    }

}
