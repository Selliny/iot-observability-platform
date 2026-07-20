package processing_service.adapters.in.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing_service.application.port.in.ProcessSensorReadingUseCase;
import processing_service.application.port.in.ProcessingMetaData;
import processing_service.application.result.ProcessingResult;

@Component
public class KafkaSensorReadingConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaSensorReadingConsumer.class);
    private final KafkaSensorReadingMapper kafkaSensorReadingMapper;
    private final ProcessSensorReadingUseCase processSensorReadingUseCase;

    public KafkaSensorReadingConsumer(
            KafkaSensorReadingMapper kafkaSensorReadingMapper,
            ProcessSensorReadingUseCase processSensorReadingUseCase) {
        this.kafkaSensorReadingMapper = kafkaSensorReadingMapper;
        this.processSensorReadingUseCase = processSensorReadingUseCase;
    }

    @KafkaListener(topics = "${app.kafka.topics.sensor-readings}", groupId = "${app.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, KafkaSensorReadingEvent> record) {
        ProcessingResult result = processSensorReadingUseCase.process(
                kafkaSensorReadingMapper.toCommand(record.value()),
                new ProcessingMetaData(record.topic(), record.partition(), record.offset()));
        log.info("Sensor reading consumed: eventId={}, partition={}, offset={}, result={}", record.value().eventId(),
                record.partition(), record.offset(), result);
    }
}
