package ingestion_service.application.usecase;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import ingestion_service.application.command.IngestSensorReadingCommand;
import ingestion_service.application.port.out.SensorReadingPublisher;
import ingestion_service.application.result.IngestionResult;
import ingestion_service.domain.model.SensorReading;
import ingestion_service.domain.model.SensorType;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngestSensorReadingServiceTest {

    @Test
    void shouldValidateAndPublishSensorReading() {
        SensorReadingPublisher publisher = mock(SensorReadingPublisher.class);

        Instant fixedTime = Instant.parse(
                "2026-07-14T15:00:00Z");

        Clock clock = Clock.fixed(
                fixedTime,
                ZoneOffset.UTC);

        IngestSensorReadingService service = new IngestSensorReadingService(
                publisher,
                clock);

        IngestSensorReadingCommand command = mock(IngestSensorReadingCommand.class);
        when(command.eventId())
                .thenReturn("01JZDXVJ10C6YPZ2Z8M51T7WEQ");
        when(command.tenantId())
                .thenReturn("tenant-01");
        when(command.plantId())
                .thenReturn("plant-01");
        when(command.lineId())
                .thenReturn("line-01");
        when(command.machineId())
                .thenReturn("machine-01");
        when(command.sensorId())
                .thenReturn("sensor-temperature-01");
        when(command.type())
                .thenReturn(SensorType.TEMPERATURE);
        when(command.value())
                .thenReturn(new BigDecimal("74.35"));
        when(command.unit())
                .thenReturn("CELSIUS");
        when(command.occurredAt())
                .thenReturn(Instant.parse("2026-07-14T14:59:59Z"));

        IngestionResult result = service.ingest(command);

        ArgumentCaptor<SensorReading> captor = ArgumentCaptor.forClass(
                SensorReading.class);

        verify(publisher).publish(captor.capture());

        SensorReading publishedReading = captor.getValue();

        assertThat(publishedReading.eventId())
                .isEqualTo(command.eventId());

        assertThat(publishedReading.ingestionAt())
                .isEqualTo(fixedTime);

        assertThat(result.status())
                .isEqualTo("ACCEPTED");

        assertThat(result.ingestionAt())
                .isEqualTo(fixedTime);
    }
}
