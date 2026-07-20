package processing_service.domain.exception;

public class InvalidSensorReadingException extends RuntimeException {
    public InvalidSensorReadingException(String message) {
        super(message);
    }
}
