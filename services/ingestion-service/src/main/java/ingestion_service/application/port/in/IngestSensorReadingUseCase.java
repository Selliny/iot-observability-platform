package ingestion_service.application.port.in;

import ingestion_service.application.command.IngestSensorReadingCommand;
import ingestion_service.application.result.IngestionResult;

public interface IngestSensorReadingUseCase {
    IngestionResult ingest(IngestSensorReadingCommand command);
}
