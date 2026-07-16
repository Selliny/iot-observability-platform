package adapters.out.mqtt;

public class SensorReadingSerializationException extends RuntimeException {
    public SensorReadingSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
