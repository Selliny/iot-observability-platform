package configuration;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {
    @Bean(destroyMethod = "disconnect")
    public MqttAsyncClient mqttClient(@Value("${app.mqtt.broker-url}") String brokerUrl,
            @Value("${app.mqtt.client-id}") String clientId, MqttConnectOptions connectOptions)
            throws MqttException {
        MqttAsyncClient client = new MqttAsyncClient(brokerUrl, clientId, new MemoryPersistence());
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
