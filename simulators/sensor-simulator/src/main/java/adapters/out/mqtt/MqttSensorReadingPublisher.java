package adapters.out.mqtt;

import java.nio.charset.StandardCharsets;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import application.port.out.SensorReadingPublisher;
import domain.model.SensorReading;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.ObjectMapper;

@Component
public class MqttSensorReadingPublisher implements SensorReadingPublisher {

    private final MqttAsyncClient mqttClient;
    private final ObjectMapper objectMapper;
    private final int qos;
    private final String topicPattern;

    public MqttSensorReadingPublisher(
            MqttAsyncClient mqttClient,
            ObjectMapper objectMapper,

            @Value("${app.mqtt.qos}") int qos,

            @Value("${app.mqtt.topic-pattern}") String topicPattern) {
        this.mqttClient = mqttClient;
        this.objectMapper = objectMapper;
        this.qos = qos;
        this.topicPattern = topicPattern;
    }

    @Override
    public void publish(SensorReading reading) {
        String topic = buildTopic(reading);

        MqttSensorReadingMessage payload = MqttSensorReadingMessage.from(reading);

        try {
            byte[] json = objectMapper
                    .writeValueAsString(payload)
                    .getBytes(StandardCharsets.UTF_8);

            MqttMessage mqttMessage = new MqttMessage(json);

            mqttMessage.setQos(qos);
            mqttMessage.setRetained(false);

            mqttClient.publish(
                    topic,
                    mqttMessage);
        } catch (DatabindException exception) {
            throw new SensorReadingSerializationException(
                    "Could not serialize sensor reading",
                    exception);
        } catch (Exception exception) {
            throw new SensorReadingPublicationException(
                    "Could not publish sensor reading to MQTT",
                    exception);
        }
    }

    private String buildTopic(SensorReading reading) {
        return topicPattern
                .replace("{tenantId}", reading.tenantId())
                .replace("{plantId}", reading.plantId())
                .replace("{lineId}", reading.lineId())
                .replace("{machineId}", reading.machineId())
                .replace("{sensorId}", reading.sensorId());
    }

}
