package ingestion_service.adapters.in.mqtt;

public class MqttTopicPayloadMismatchException extends RuntimeException {
    public MqttTopicPayloadMismatchException(String message) {
        super(message);
    }

    public MqttTopicPayloadMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

}
