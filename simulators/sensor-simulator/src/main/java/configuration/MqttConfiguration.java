package configuration;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MqttConfiguration.class);

    @Bean(destroyMethod = "disconnect")
    public MqttAsyncClient mqttClient(@Value("${app.mqtt.broker-url}") String brokerUrl,
            @Value("${app.mqtt.client-id}") String clientId, MqttConnectOptions connectOptions)
            throws MqttException {
        MqttAsyncClient client = new MqttAsyncClient(brokerUrl, clientId, new MemoryPersistence());

        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                log.info("MQTT connection {} to {}", reconnect ? "re-established" : "established", serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                log.warn("MQTT connection lost, automatic reconnect will be attempted", cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // Publisher-only client: no inbound subscriptions expected.
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Fire-and-forget publishing: no delivery tracking needed.
            }
        });

        client.connect(connectOptions);
        return client;
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions(
            @Value("${app.mqtt.connection-timeout-seconds}") int connectionTimeout,
            @Value("${app.mqtt.keep-alive-seconds}") int keepAlive) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(connectionTimeout);
        options.setKeepAliveInterval(keepAlive);
        return options;
    }
}
