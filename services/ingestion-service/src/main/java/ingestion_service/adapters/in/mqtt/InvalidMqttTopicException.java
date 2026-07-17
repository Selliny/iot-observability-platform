package ingestion_service.adapters.in.mqtt;

public class InvalidMqttTopicException extends RuntimeException {
    public InvalidMqttTopicException(String message) {
        super(message);
    }

}
