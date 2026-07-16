package adapters.out.mqtt;

public class SensorReadingPublicationException extends RuntimeException {
    public SensorReadingPublicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
