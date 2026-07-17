package ingestion_service.adapters.in.mqtt;

import java.time.Clock;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import ingestion_service.application.port.in.IngestSensorReadingUseCase;
import ingestion_service.application.port.out.IngestionMetrics;
import ingestion_service.application.port.out.RejectedSensorMessagePublisher;
import ingestion_service.domain.exception.InvalidSensorReadingException;
import ingestion_service.domain.model.RejectedSensorMessage;
import tools.jackson.databind.ObjectMapper;

@Component
public class MqttSensorReadingHandler {
    private static final Logger log = LoggerFactory.getLogger(MqttSensorReadingHandler.class);
    private static final String SOURCE = "mqtt";
    private final MqttTopicParser topicParser;
    private final MqttTopicPayloadValidator topicPayloadValidator;
    private final ObjectMapper objectMapper;
    private final MqttSensorReadingMapper mqttSensorReadingMapper;
    private final IngestSensorReadingUseCase ingestSensorReadingUseCase;
    private final RejectedSensorMessagePublisher rejectedSensorMessagePublisher;
    private final IngestionMetrics ingestionMetrics;
    private final Clock clock;

    public MqttSensorReadingHandler(
            ObjectMapper objectMapper,
            MqttSensorReadingMapper mqttSensorReadingMapper,
            IngestSensorReadingUseCase ingestSensorReadingUseCase,
            RejectedSensorMessagePublisher rejectedSensorMessagePublisher,
            IngestionMetrics ingestionMetrics,
            Clock clock, MqttTopicParser topicParser,
            MqttTopicPayloadValidator topicPayloadValidator) {
        this.objectMapper = objectMapper;
        this.mqttSensorReadingMapper = mqttSensorReadingMapper;
        this.ingestSensorReadingUseCase = ingestSensorReadingUseCase;
        this.rejectedSensorMessagePublisher = rejectedSensorMessagePublisher;
        this.ingestionMetrics = ingestionMetrics;
        this.clock = clock;
        this.topicParser = topicParser;
        this.topicPayloadValidator = topicPayloadValidator;
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handle(Message<?> message) {
        String payload = String.valueOf(message.getPayload());
        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);

        MqttSensorReadingPayload mqttPayload;
        try {
            mqttPayload = objectMapper.readValue(payload, MqttSensorReadingPayload.class);
        } catch (Exception exception) {
            reject("INVALID_JSON", exception.getMessage(), payload, topic);
            return;
        }

        try {
            ingestSensorReadingUseCase.ingest(mqttSensorReadingMapper.toCommand(mqttPayload));
            ingestionMetrics.registerAccepted();
            log.info("Successfully ingested sensor reading: eventId={}, topic={}", mqttPayload.eventId(), topic);
        } catch (UnsupportedMqttSchemaVersionException exception) {
            reject("UNSUPPORTED_SCHEMA_VERSION", exception.getMessage(), payload, topic);
        } catch (InvalidContractException exception) {
            reject("INVALID_CONTRACT", exception.getMessage(), payload, topic);
        } catch (InvalidSensorReadingException exception) {
            reject("INVALID_DOMAIN_DATA", exception.getMessage(), payload, topic);
        } catch (Exception exception) {
            reject("INGESTION_FAILED", exception.getMessage(), payload, topic);
        }
    }

    private void reject(String reasonCode, String reason, String rawPayload, String topic) {
        RejectedSensorMessage rejectedSensorMessage = new RejectedSensorMessage(
                UUID.randomUUID().toString(),
                SOURCE,
                topic,
                reasonCode,
                reason,
                rawPayload,
                clock.instant());

        rejectedSensorMessagePublisher.publish(rejectedSensorMessage);
        ingestionMetrics.registerRejected(reasonCode);

        log.warn("Rejected sensor message: reasonCode={}, topic={}", reasonCode, topic);
    }
}
