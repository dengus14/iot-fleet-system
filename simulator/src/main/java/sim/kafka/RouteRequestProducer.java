package sim.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import sim.dto.RouteRequestDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;

public class RouteRequestProducer {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "route-requests";

    public RouteRequestProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        this.producer = new KafkaProducer<>(props);
        this.objectMapper = new ObjectMapper();

        System.out.println("RouteRequestProducer initialized");
    }

    public String sendRouteRequest(Integer deviceNumber, Integer currentLocation, Integer destination) {
        try {
            String requestId = UUID.randomUUID().toString();

            RouteRequestDTO dto = RouteRequestDTO.builder()
                    .requestId(requestId)
                    .deviceNumber(deviceNumber)
                    .currentLocation(currentLocation)
                    .destination(destination)
                    .timestamp(System.currentTimeMillis())
                    .build();

            String json = objectMapper.writeValueAsString(dto);

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, requestId, json);


            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Failed to send route request: " + exception.getMessage());
                } else {
                    System.out.println("Route request sent successfully to partition " + metadata.partition());
                }
            });

            System.out.println("Sent route request: " + requestId + " for device " + deviceNumber +
                    " from " + currentLocation + " to " + destination);

            return requestId;

        } catch (Exception e) {
            System.err.println("Error sending route request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        if (producer != null) {
            producer.flush();
            producer.close();
            System.out.println("RouteRequestProducer closed");
        }
    }
}