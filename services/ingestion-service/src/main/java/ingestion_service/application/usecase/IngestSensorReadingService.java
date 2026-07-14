package ingestion_service.application.usecase;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import ingestion_service.application.command.IngestSensorReadingCommand;
import ingestion_service.application.port.in.IngestSensorReadingUseCase;
import ingestion_service.application.port.out.SensorReadingPublisher;
import ingestion_service.application.result.IngestionResult;
import ingestion_service.domain.model.SensorReading;

public class IngestSensorReadingService implements IngestSensorReadingUseCase {
    private final SensorReadingPublisher publisher;
    private final Clock clock;

    public IngestSensorReadingService(SensorReadingPublisher publisher, Clock clock) {
        this.publisher = Objects.requireNonNull(publisher);
        this.clock = Objects.requireNonNull(clock);

    }

    @Override
    public IngestionResult ingest(IngestSensorReadingCommand command) {
        Objects.requireNonNull(command, "command cannot be null");
        Instant ingestedAt = clock.instant();
        SensorReading sensorReading = new SensorReading(command.eventId(), command.tenantId(), command.plantId(),
                command.lineId(), command.machineId(), command.sensorId(), command.type(), command.value(),
                command.unit(), command.sequenceNumber(), command.occurredAt(), ingestedAt);
        publisher.publish(sensorReading);
        return IngestionResult.accepted(sensorReading.eventId(), ingestedAt);
    }

}
