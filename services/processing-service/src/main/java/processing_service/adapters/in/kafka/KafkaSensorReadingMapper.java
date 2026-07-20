package processing_service.adapters.in.kafka;

import org.springframework.stereotype.Component;

import processing_service.application.command.ProcessSensorReadingCommand;
import processing_service.domain.model.SensorType;

@Component
public class KafkaSensorReadingMapper {
    public ProcessSensorReadingCommand toCommand(KafkaSensorReadingEvent event) {
        if (event.schemaVersion() != 1) {
            throw new IllegalArgumentException("Unsupported schema version: " + event.schemaVersion());
        }
        return new ProcessSensorReadingCommand(
                event.eventId(),
                event.tenantId(),
                event.plantId(),
                event.lineId(),
                event.machineId(),
                event.sensorId(),
                SensorType.valueOf(event.type()),
                event.value(),
                event.unit(),
                event.sequenceNumber(),
                event.occurredAt(),
                event.ingestedAt());
    }

}
