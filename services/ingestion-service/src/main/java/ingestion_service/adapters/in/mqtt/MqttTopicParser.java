package ingestion_service.adapters.in.mqtt;

import org.springframework.stereotype.Component;

@Component
public class MqttTopicParser {
    private static final int ExpectedLevels = 7;
    private static final int RootIndex = 0;
    private static final int TenantIndex = 1;
    private static final int PlantIndex = 2;
    private static final int LineIndex = 3;
    private static final int MachineIndex = 4;
    private static final int SensorIndex = 5;
    private static final int EventTypeIndex = 6;
    private static final String ExpectedRoot = "iot";
    private static final String ExpectedEventType = "readings";

    public MqttTopicIdentity parse(String topic) {
        if (topic == null || topic.isBlank()) {
            throw new InvalidMqttTopicException("Topic is required");
        }
        String[] levels = topic.split("/", -1);
        if (levels.length != ExpectedLevels) {
            throw new InvalidMqttTopicException("Invalid topic format: " + topic);
        }
        if (!ExpectedRoot.equals(levels[RootIndex])) {
            throw new InvalidMqttTopicException(
                    "MQTT topic must start with 'iot'");
        }

        if (!ExpectedEventType.equals(
                levels[EventTypeIndex])) {
            throw new InvalidMqttTopicException(
                    "MQTT topic must end with 'readings'");
        }

        validateLevel(levels[TenantIndex], "tenantId");
        validateLevel(levels[PlantIndex], "plantId");
        validateLevel(levels[LineIndex], "lineId");
        validateLevel(levels[MachineIndex], "machineId");
        validateLevel(levels[SensorIndex], "sensorId");

        return new MqttTopicIdentity(
                levels[TenantIndex],
                levels[PlantIndex],
                levels[LineIndex],
                levels[MachineIndex],
                levels[SensorIndex]);
    }

    private void validateLevel(
            String value,
            String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidMqttTopicException(
                    fieldName + " is missing from MQTT topic");
        }

        if (value.contains("+") || value.contains("#")) {
            throw new InvalidMqttTopicException(
                    fieldName + " cannot contain MQTT wildcards");
        }
    }

}
