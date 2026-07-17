package ingestion_service.adapters.out.mqtt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import ingestion_service.adapters.in.mqtt.MqttSensorReadingHandler;
import ingestion_service.adapters.in.mqtt.MqttSensorReadingMapper;
import ingestion_service.application.port.in.IngestSensorReadingUseCase;
import ingestion_service.application.port.out.IngestionMetrics;
import ingestion_service.application.port.out.RejectedSensorMessagePublisher;
import ingestion_service.domain.model.RejectedSensorMessage;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

class MqttSensorReadingHandlerTest {

        @Test
        void shouldRejectInvalidJsonWithoutCallingUseCase() {
                ObjectMapper objectMapper = new JsonMapper();

                MqttSensorReadingMapper mapper = new MqttSensorReadingMapper();

                IngestSensorReadingUseCase useCase = mock(IngestSensorReadingUseCase.class);

                RejectedSensorMessagePublisher rejectedPublisher = mock(RejectedSensorMessagePublisher.class);

                IngestionMetrics metrics = mock(IngestionMetrics.class);

                Clock clock = Clock.fixed(
                                Instant.parse("2026-07-17T12:00:00Z"),
                                ZoneOffset.UTC);

                MqttSensorReadingHandler handler = new MqttSensorReadingHandler(
                                objectMapper,
                                mapper,
                                useCase,
                                rejectedPublisher,
                                metrics,
                                clock);

                Message<String> message = MessageBuilder
                                .withPayload("{\"eventId\":")
                                .setHeader(
                                                "mqtt_receivedTopic",
                                                "iot/tenant/plant/line/machine/sensor/readings")
                                .build();

                handler.handle(message);

                verifyNoInteractions(useCase);

                ArgumentCaptor<RejectedSensorMessage> captor = ArgumentCaptor.forClass(
                                RejectedSensorMessage.class);

                verify(rejectedPublisher)
                                .publish(captor.capture());

                assertThat(captor.getValue().reasonCode())
                                .isEqualTo("INVALID_JSON");

                assertThat(captor.getValue().rawPayload())
                                .isEqualTo("{\"eventId\":");

                verify(metrics)
                                .registerRejected("INVALID_JSON");
        }
}
