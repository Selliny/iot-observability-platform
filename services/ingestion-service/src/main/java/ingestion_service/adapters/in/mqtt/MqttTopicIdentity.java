package ingestion_service.adapters.in.mqtt;

public record MqttTopicIdentity(String tenantId, String plantId, String lineId, String machineId, String sensorId) {

}
