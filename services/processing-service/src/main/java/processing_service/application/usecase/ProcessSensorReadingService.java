package processing_service.application.usecase;

import java.beans.Transient;
import java.time.Clock;
import java.time.Instant;
import org.springframework.transaction.annotation.Transactional;

import processing_service.application.command.ProcessSensorReadingCommand;
import processing_service.application.port.in.ProcessSensorReadingUseCase;
import processing_service.application.port.in.ProcessingMetaData;
import processing_service.application.port.out.ProcessedEventRepository;
import processing_service.application.port.out.SensorReadingRepository;
import processing_service.application.result.ProcessingResult;
import processing_service.domain.model.SensorReading;

public class ProcessSensorReadingService implements ProcessSensorReadingUseCase {
    private final ProcessedEventRepository processedEventRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final Clock clock;

    public ProcessSensorReadingService(ProcessedEventRepository processedEventRepository,
            SensorReadingRepository sensorReadingRepository, Clock clock) {
        this.processedEventRepository = processedEventRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public ProcessingResult process(ProcessSensorReadingCommand command, ProcessingMetaData metadata) {
        Instant processedAt = clock.instant();
        boolean registered = processedEventRepository.registerIfNotExists(
                command.eventId(),
                metadata.topic(),
                metadata.partition(),
                metadata.offset(),
                processedAt);

        if (!registered) {
            return ProcessingResult.DUPLICATE;
        }

        SensorReading reading = new SensorReading(
                command.eventId(),
                command.tenantId(),
                command.plantId(),
                command.lineId(),
                command.machineId(),
                command.sensorId(),
                command.type(),
                command.value(),
                command.unit(),
                command.sequenceNumber(),
                command.occurredAt(),
                command.ingestedAt(),
                processedAt);

        sensorReadingRepository.save(reading);

        return ProcessingResult.PROCESSED;
    }

}
