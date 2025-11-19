package sim.core;

import lombok.Data;

import java.util.Random;

@Data
public class Device {
    // --- Identity ---
    private Long id;
    private Integer deviceNumber;

    // --- Engine & motion ---
    private Double speed;
    private Boolean engineOn;
    private Double engineTemp;
    private Double fuelLevel;

    // --- Location & route state ---
    private Integer currentNodeId;
    private Integer startLocation;
    private Integer nextNodeId;   // optional for movement step
    private Integer routeIndex;     // pointer inside a route

    // --- Metadata ---
    private DeviceState state;
    private DeviceType deviceType;

    // --- Simulation runtime fields ---
    private long lastUpdateTimestamp;   // for delta-time calculations
    private long lastTelemetryTimestamp;
    private double distanceIntoEdge;    // how far along the graph edge we are
    private final Random random = new Random();
}
