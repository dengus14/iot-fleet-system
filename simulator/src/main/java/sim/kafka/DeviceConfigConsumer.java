package sim.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import sim.core.Device;
import sim.core.DeviceFactory;
import sim.dto.DeviceConfigDTO;
import sim.dto.RouteCommandDTO;
import sim.registry.DeviceRegistry;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class DeviceConfigConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;
    private final DeviceRegistry deviceRegistry;
    private static final String TOPIC = "device-create-events";
    private volatile boolean running = true;

    public DeviceConfigConsumer(DeviceRegistry deviceRegistry){
        this.deviceRegistry = deviceRegistry;
        objectMapper=new ObjectMapper();
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "simulator-group-config");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList(TOPIC));
        System.out.println("RouteCommandConsumer initialized and subscribed to: " + TOPIC);
    }

    public void startListening() {
  
        Thread consumerThread = new Thread(() -> {
            System.out.println("DeviceConfigConsumer started listening...");

            while (running) {
                try {
                    // Poll for messages
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));

                    for (ConsumerRecord<String, String> record : records) {
                        handleConfig(record.value());
                    }

                } catch (Exception e) {
                    System.err.println("Error polling messages: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("DeviceConfigConsumer stopped");
        },"DeviceConfigConsumerThread");

        consumerThread.setDaemon(true);
       consumerThread.start();



    }

    private void handleConfig(String json) {
        try {
            DeviceConfigDTO dto = objectMapper.readValue(json, DeviceConfigDTO.class);
            Device device = DeviceFactory.fromConfig(dto);
            System.out.println("Received new device via Kafka: " + dto.getDeviceId());

            deviceRegistry.addOrUpdateDevice(device);

        } catch (Exception e) {
            System.err.println("Failed to handle command: " + e.getMessage());
            e.printStackTrace();
        }

    }
    public void stop() {
        running = false;
    }

    public void close() {

        if (consumer != null) {
            consumer.close();
            System.out.println("DeviceConfigConsumer closed");
        }
    }

}
