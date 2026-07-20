package ingestion_service.adapters.in.mqtt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MqttTopicParserTest {

    private final MqttTopicParser parser = new MqttTopicParser();

    @Test
    void shouldExtractTheFiveIdentifiersFromAWellFormedTopic() {
        MqttTopicIdentity identity = parser.parse("iot/tenant-01/plant-01/line-01/machine-01/sensor-01/readings");

        assertThat(identity.tenantId()).isEqualTo("tenant-01");
        assertThat(identity.plantId()).isEqualTo("plant-01");
        assertThat(identity.lineId()).isEqualTo("line-01");
        assertThat(identity.machineId()).isEqualTo("machine-01");
        assertThat(identity.sensorId()).isEqualTo("sensor-01");
    }

    @Test
    void shouldRejectTopicWithWrongNumberOfLevels() {
        assertThatThrownBy(() -> parser.parse("iot/tenant-01/plant-01/readings"))
                .isInstanceOf(InvalidMqttTopicException.class);
    }

    @Test
    void shouldRejectTopicWithEmptyLevel() {
        assertThatThrownBy(() -> parser.parse("iot/tenant-01//line-01/machine-01/sensor-01/readings"))
                .isInstanceOf(InvalidMqttTopicException.class);
    }

    @Test
    void shouldRejectTopicWithRootDifferentFromIot() {
        assertThatThrownBy(() -> parser.parse("other/tenant-01/plant-01/line-01/machine-01/sensor-01/readings"))
                .isInstanceOf(InvalidMqttTopicException.class);
    }

    @Test
    void shouldRejectTopicNotEndingInReadings() {
        assertThatThrownBy(() -> parser.parse("iot/tenant-01/plant-01/line-01/machine-01/sensor-01/events"))
                .isInstanceOf(InvalidMqttTopicException.class);
    }

    @Test
    void shouldRejectTopicWithWildcard() {
        assertThatThrownBy(() -> parser.parse("iot/tenant-01/+/line-01/machine-01/sensor-01/readings"))
                .isInstanceOf(InvalidMqttTopicException.class);
    }
}
