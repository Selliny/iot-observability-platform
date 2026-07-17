package ingestion_service.adapters.in.mqtt;

public class UnsupportedMqttSchemaVersionException extends RuntimeException {
    public UnsupportedMqttSchemaVersionException(String message) {
        super(message);
    }

    public UnsupportedMqttSchemaVersionException(String message, Throwable cause) {
        super(message, cause);
    }

}
