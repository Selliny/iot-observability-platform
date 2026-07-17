package ingestion_service.adapters.in.mqtt;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class MqttTopicPayloadValidator {
    public void validate(
            MqttTopicIdentity topicIdentity,
            MqttSensorReadingPayload payload) {
        validateField(
                "tenantId",
                topicIdentity.tenantId(),
                payload.tenantId());

        validateField(
                "plantId",
                topicIdentity.plantId(),
                payload.plantId());

        validateField(
                "lineId",
                topicIdentity.lineId(),
                payload.lineId());

        validateField(
                "machineId",
                topicIdentity.machineId(),
                payload.machineId());

        validateField(
                "sensorId",
                topicIdentity.sensorId(),
                payload.sensorId());
    }

    private void validateField(
            String fieldName,
            String topicValue,
            String payloadValue) {
        if (!Objects.equals(topicValue, payloadValue)) {
            throw new MqttTopicPayloadMismatchException(
                    fieldName
                            + " from MQTT topic does not match payload");
        }
    }
}
