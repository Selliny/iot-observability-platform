package ingestion_service.adapters.in.mqtt;

public class InvalidContractException extends RuntimeException {
    public InvalidContractException(String message) {
        super(message);
    }
}
