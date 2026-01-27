package sim.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import sim.dto.DeviceTelemetryDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class TelemetryProducer {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "device-events";

    public TelemetryProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        this.producer = new KafkaProducer<>(props);
        this.objectMapper = new ObjectMapper();

        System.out.println("TelemetryProducer initialized");
    }

    public void publishTelemetry(DeviceTelemetryDTO dto) {
        try {

            String json = objectMapper.writeValueAsString(dto);

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, dto.getDeviceId().toString(), json);


            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Failed to send telemetry request: " + exception.getMessage());
                } else {
                    System.out.println("Telemetry request sent successfully to partition " + metadata.partition());
                }
            });

            System.out.println("Sent telemetry request  " + "for device " + dto.getDeviceId());

        } catch (Exception e) {
            System.err.println("Error sending telemetry request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        if (producer != null) {
            producer.flush();
            producer.close();
            System.out.println("TelemetryProducer closed");
        }
    }
}