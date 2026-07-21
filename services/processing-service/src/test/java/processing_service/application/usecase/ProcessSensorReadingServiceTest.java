package processing_service.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import processing_service.application.command.ProcessSensorReadingCommand;
import processing_service.application.port.in.ProcessingMetaData;
import processing_service.application.port.out.ProcessedEventRepository;
import processing_service.application.port.out.SensorReadingRepository;
import processing_service.application.result.ProcessingResult;
import processing_service.domain.model.SensorReading;
import processing_service.domain.model.SensorType;

class ProcessSensorReadingServiceTest {

    private static final Instant FIXED_NOW = Instant.parse("2026-07-21T12:00:00Z");

    private final ProcessedEventRepository processedEventRepository = mock(ProcessedEventRepository.class);
    private final SensorReadingRepository sensorReadingRepository = mock(SensorReadingRepository.class);
    private final Clock clock = Clock.fixed(FIXED_NOW, ZoneOffset.UTC);

    private final ProcessSensorReadingService service = new ProcessSensorReadingService(
            processedEventRepository, sensorReadingRepository, clock);

    @Test
    void shouldPersistReadingAndReturnProcessedWhenEventIsNew() {
        when(processedEventRepository.registerIfNotExists(any(), any(), anyInt(), anyLong(), any()))
                .thenReturn(true);

        ProcessSensorReadingCommand command = command();
        ProcessingMetaData metadata = new ProcessingMetaData("iot.sensor-readings.v1", 2, 381L);

        ProcessingResult result = service.process(command, metadata);

        assertThat(result).isEqualTo(ProcessingResult.PROCESSED);
        verify(processedEventRepository).registerIfNotExists("evt-1", "iot.sensor-readings.v1", 2, 381L, FIXED_NOW);
        verify(sensorReadingRepository).save(any(SensorReading.class));
    }

    @Test
    void shouldNotPersistReadingAndReturnDuplicateWhenEventAlreadyProcessed() {
        when(processedEventRepository.registerIfNotExists(any(), any(), anyInt(), anyLong(), any()))
                .thenReturn(false);

        ProcessSensorReadingCommand command = command();
        ProcessingMetaData metadata = new ProcessingMetaData("iot.sensor-readings.v1", 2, 381L);

        ProcessingResult result = service.process(command, metadata);

        assertThat(result).isEqualTo(ProcessingResult.DUPLICATE);
        verify(sensorReadingRepository, never()).save(any());
    }

    private static ProcessSensorReadingCommand command() {
        return new ProcessSensorReadingCommand(
                "evt-1",
                "tenant-01",
                "plant-01",
                "line-01",
                "machine-01",
                "sensor-01",
                SensorType.TEMPERATURE,
                BigDecimal.valueOf(55.5),
                "Celsius",
                1,
                Instant.parse("2026-07-21T11:59:00Z"),
                Instant.parse("2026-07-21T11:59:30Z"));
    }
}
