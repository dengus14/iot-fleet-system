package sim.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import sim.dto.RouteCommandDTO;
import sim.registry.DeviceRegistry;
import sim.core.Device;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class RouteCommandConsumer {

    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;
    private final DeviceRegistry deviceRegistry;
    private static final String TOPIC = "route-commands";
    private volatile boolean running = true;

    public RouteCommandConsumer(DeviceRegistry deviceRegistry) {
        this.deviceRegistry = deviceRegistry;
        this.objectMapper = new ObjectMapper();

        // Configure Kafka consumer
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "simulator-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList(TOPIC));

        System.out.println("RouteCommandConsumer initialized and subscribed to: " + TOPIC);
    }

    public void startListening() {
        // Start consumer in a separate thread
        Thread consumerThread = new Thread(() -> {
            System.out.println("RouteCommandConsumer started listening...");

            while (running) {
                try {
                    // Poll for messages
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                    for (ConsumerRecord<String, String> record : records) {
                        handleRouteCommand(record.value());
                    }

                } catch (Exception e) {
                    System.err.println("Error polling messages: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("RouteCommandConsumer stopped");
        });

        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    private void handleRouteCommand(String json) {
        try {
            RouteCommandDTO dto = objectMapper.readValue(json, RouteCommandDTO.class);

            System.out.println("Received route command: " + dto.getRequestId() +
                    " for device " + dto.getDeviceNumber() +
                    " with route: " + dto.getPlannedRoute());

            Device device = deviceRegistry.getDeviceById(dto.getDeviceNumber().longValue());

            if (device != null) {
                device.setPlannedRoute(dto.getPlannedRoute());
                device.setRouteIndex(0);

                System.out.println("Device " + device.getDeviceNumber() +
                        " assigned route: " + dto.getPlannedRoute());
            } else {
                System.err.println("Device not found: " + dto.getDeviceNumber());
            }

        } catch (Exception e) {
            System.err.println("Failed to handle route command: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
    }

    public void close() {
        stop();
        if (consumer != null) {
            consumer.close();
            System.out.println("RouteCommandConsumer closed");
        }
    }
}