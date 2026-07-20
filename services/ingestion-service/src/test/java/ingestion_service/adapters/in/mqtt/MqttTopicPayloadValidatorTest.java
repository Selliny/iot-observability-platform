package ingestion_service.adapters.in.mqtt;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import ingestion_service.domain.model.SensorType;

class MqttTopicPayloadValidatorTest {

    private final MqttTopicPayloadValidator validator = new MqttTopicPayloadValidator();

    private static final MqttTopicIdentity IDENTITY = new MqttTopicIdentity(
            "tenant-01", "plant-01", "line-01", "machine-01", "sensor-01");

    @Test
    void shouldAcceptWhenTopicAndPayloadIdentityMatch() {
        MqttSensorReadingPayload payload = payloadWith(
                "tenant-01", "plant-01", "line-01", "machine-01", "sensor-01");

        assertThatCode(() -> validator.validate(IDENTITY, payload)).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectWhenTenantIdDiffers() {
        MqttSensorReadingPayload payload = payloadWith(
                "other-tenant", "plant-01", "line-01", "machine-01", "sensor-01");

        assertThatThrownBy(() -> validator.validate(IDENTITY, payload))
                .isInstanceOf(MqttTopicPayloadMismatchException.class);
    }

    @Test
    void shouldRejectWhenPlantIdDiffers() {
        MqttSensorReadingPayload payload = payloadWith(
                "tenant-01", "other-plant", "line-01", "machine-01", "sensor-01");

        assertThatThrownBy(() -> validator.validate(IDENTITY, payload))
                .isInstanceOf(MqttTopicPayloadMismatchException.class);
    }

    @Test
    void shouldRejectWhenLineIdDiffers() {
        MqttSensorReadingPayload payload = payloadWith(
                "tenant-01", "plant-01", "other-line", "machine-01", "sensor-01");

        assertThatThrownBy(() -> validator.validate(IDENTITY, payload))
                .isInstanceOf(MqttTopicPayloadMismatchException.class);
    }

    @Test
    void shouldRejectWhenMachineIdDiffers() {
        MqttSensorReadingPayload payload = payloadWith(
                "tenant-01", "plant-01", "line-01", "other-machine", "sensor-01");

        assertThatThrownBy(() -> validator.validate(IDENTITY, payload))
                .isInstanceOf(MqttTopicPayloadMismatchException.class);
    }

    @Test
    void shouldRejectWhenSensorIdDiffers() {
        MqttSensorReadingPayload payload = payloadWith(
                "tenant-01", "plant-01", "line-01", "machine-01", "other-sensor");

        assertThatThrownBy(() -> validator.validate(IDENTITY, payload))
                .isInstanceOf(MqttTopicPayloadMismatchException.class);
    }

    private static MqttSensorReadingPayload payloadWith(
            String tenantId, String plantId, String lineId, String machineId, String sensorId) {
        return new MqttSensorReadingPayload(
                1,
                "evt-1",
                tenantId,
                plantId,
                lineId,
                machineId,
                sensorId,
                SensorType.TEMPERATURE,
                BigDecimal.TEN,
                "Celsius",
                1,
                Instant.parse("2026-07-17T12:00:00Z"));
    }
}
