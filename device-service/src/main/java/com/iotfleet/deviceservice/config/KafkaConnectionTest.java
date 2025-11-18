package com.iotfleet.deviceservice.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaConnectionTest implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== Testing Kafka Connection ===");

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(props)) {
            System.out.println("✅ Successfully connected to Kafka!");
            System.out.println("Cluster ID: " + adminClient.describeCluster().clusterId().get());
            System.out.println("================================\n");
        } catch (Exception e) {
            System.out.println("❌ Failed to connect to Kafka: " + e.getMessage());
            System.out.println("================================\n");
        }
    }
}