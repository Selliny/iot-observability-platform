package ingestion_service.adapters.in.mqtt;

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

import ingestion_service.application.port.in.IngestSensorReadingUseCase;
import ingestion_service.application.port.out.IngestionMetrics;
import ingestion_service.application.port.out.RejectedSensorMessagePublisher;
import ingestion_service.domain.model.RejectedSensorMessage;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

class MqttSensorReadingHandlerTest {

    private final ObjectMapper objectMapper = new JsonMapper();
    private final MqttSensorReadingMapper mapper = new MqttSensorReadingMapper();
    private final MqttTopicParser topicParser = new MqttTopicParser();
    private final MqttTopicPayloadValidator topicPayloadValidator = new MqttTopicPayloadValidator();
    private final Clock clock = Clock.fixed(
            Instant.parse("2026-07-17T12:00:00Z"),
            ZoneOffset.UTC);

    @Test
    void shouldRejectInvalidJsonWithoutCallingUseCase() {
        IngestSensorReadingUseCase useCase = mock(IngestSensorReadingUseCase.class);
        RejectedSensorMessagePublisher rejectedPublisher = mock(RejectedSensorMessagePublisher.class);
        IngestionMetrics metrics = mock(IngestionMetrics.class);

        MqttSensorReadingHandler handler = new MqttSensorReadingHandler(
                objectMapper,
                mapper,
                useCase,
                rejectedPublisher,
                metrics,
                clock,
                topicParser,
                topicPayloadValidator);

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

    @Test
    void shouldRejectMalformedTopicWithoutParsingPayload() {
        IngestSensorReadingUseCase useCase = mock(IngestSensorReadingUseCase.class);
        RejectedSensorMessagePublisher rejectedPublisher = mock(RejectedSensorMessagePublisher.class);
        IngestionMetrics metrics = mock(IngestionMetrics.class);

        MqttSensorReadingHandler handler = new MqttSensorReadingHandler(
                objectMapper,
                mapper,
                useCase,
                rejectedPublisher,
                metrics,
                clock,
                topicParser,
                topicPayloadValidator);

        Message<String> message = MessageBuilder
                .withPayload("{}")
                .setHeader(
                        "mqtt_receivedTopic",
                        "iot/tenant/plant/readings")
                .build();

        handler.handle(message);

        verifyNoInteractions(useCase);

        ArgumentCaptor<RejectedSensorMessage> captor = ArgumentCaptor.forClass(
                RejectedSensorMessage.class);

        verify(rejectedPublisher)
                .publish(captor.capture());

        assertThat(captor.getValue().reasonCode())
                .isEqualTo("INVALID_MQTT_TOPIC");

        verify(metrics)
                .registerRejected("INVALID_MQTT_TOPIC");
    }

    @Test
    void shouldRejectWhenTopicIdentityDoesNotMatchPayload() {
        IngestSensorReadingUseCase useCase = mock(IngestSensorReadingUseCase.class);
        RejectedSensorMessagePublisher rejectedPublisher = mock(RejectedSensorMessagePublisher.class);
        IngestionMetrics metrics = mock(IngestionMetrics.class);

        MqttSensorReadingHandler handler = new MqttSensorReadingHandler(
                objectMapper,
                mapper,
                useCase,
                rejectedPublisher,
                metrics,
                clock,
                topicParser,
                topicPayloadValidator);

        String payload = "{\"schemaVersion\":1,\"eventId\":\"evt-1\",\"tenantId\":\"tenant\","
                + "\"plantId\":\"plant\",\"lineId\":\"line\",\"machineId\":\"machine\","
                + "\"sensorId\":\"different-sensor\",\"type\":\"TEMPERATURE\",\"value\":10.0,"
                + "\"unit\":\"Celsius\",\"sequenceNumber\":1,\"occurredAt\":\"2026-07-17T12:00:00Z\"}";

        Message<String> message = MessageBuilder
                .withPayload(payload)
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
                .isEqualTo("TOPIC_PAYLOAD_MISMATCH");

        verify(metrics)
                .registerRejected("TOPIC_PAYLOAD_MISMATCH");
    }
}
