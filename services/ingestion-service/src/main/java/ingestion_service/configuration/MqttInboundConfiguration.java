package ingestion_service.configuration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MqttInboundConfiguration {
    @Bean
    public MqttConnectOptions mqttConnectOptions(@Value("${app.mqtt.connection-timeout-seconds}") int connectionTimeout,
            @Value("${app.mqtt.keep-alive-seconds}") int keepAlive) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(connectionTimeout);
        options.setKeepAliveInterval(keepAlive);

        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory(MqttConnectOptions connectOptions) {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(connectOptions);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInboundAdapter(
            @Value("${app.mqtt.broker-url}") String brokerUrl,
            @Value("${app.mqtt.client-id}") String clientId,
            @Value("${app.mqtt.topic}") String topic,
            @Value("${app.mqtt.qos}") int qos,
            MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                brokerUrl,
                clientId,
                mqttClientFactory,
                topic);

        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();

        converter.setPayloadAsBytes(false);

        adapter.setConverter(converter);
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel());

        return adapter;
    }
}

// O adapter assina o tópico MQTT e envia as mensagens recebidas para um canal
// Spring Integration.
// O handler conectado ao canal converte o payload e chama o caso de uso.
// com as seguintes responsabilidades:
// receber mensagem
// extrair payload
// desserializar JSON
// converter para command
// chamar o caso de uso
// registrar resultado
