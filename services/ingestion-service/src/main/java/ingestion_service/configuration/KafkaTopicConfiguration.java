package ingestion_service.configuration;

import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;

@Configuration
public class KafkaTopicConfiguration {
    @Bean
    public NewTopic sensorReadingsTopic(
            @Value("${app.kafka.topics.sensor-readings.name}") String topicName,
            @Value("${app.kafka.topics.sensor-readings.partitions}") int partitions,
            @Value("${app.kafka.topics.sensor-readings.replication-factor}") short replicationFactor,
            @Value("${app.kafka.topics.sensor-readings.retention}") Duration retention) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .configs(Map.of(
                        "retention.ms",
                        String.valueOf(retention.toMillis()),
                        "cleanup.policy",
                        "delete"))
                .build();
    }

    @Bean
    public NewTopic rejectedSensorMessagesTopic(
            @Value("${app.kafka.topics.rejected-sensor-messages.name}") String topicName,
            @Value("${app.kafka.topics.rejected-sensor-messages.partitions}") int partitions,
            @Value("${app.kafka.topics.rejected-sensor-messages.replication-factor}") short replicationFactor,
            @Value("${app.kafka.topics.rejected-sensor-messages.retention}") Duration retention) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .configs(Map.of(
                        "retention.ms",
                        String.valueOf(retention.toMillis()),
                        "cleanup.policy",
                        "delete"))
                .build();
    }
}
