package processing_service.application.port.in;

public record ProcessingMetaData(String topic, int partition, long offset) {

}
