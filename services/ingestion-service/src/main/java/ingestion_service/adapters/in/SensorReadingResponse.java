package ingestion_service.adapters.in;

import java.time.Instant;

import ingestion_service.application.result.IngestionResult;

public record SensorReadingResponse(String eventId, String status, Instant ingestedAt) {
    public static SensorReadingResponse from(IngestionResult result) {
        return new SensorReadingResponse(result.eventId(), result.status(), result.ingestionAt());
    }
}
