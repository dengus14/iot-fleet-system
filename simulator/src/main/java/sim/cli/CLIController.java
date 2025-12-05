package sim.cli;

import sim.core.DeviceState;
import sim.core.DeviceType;
import sim.dto.DeviceTelemetryDTO;
import sim.graph.UndirectedNode;
import sim.kafka.TelemetryProducer;
import sim.registry.DeviceRegistry;
import sim.kafka.RouteRequestProducer;
import sim.graph.UndirectedGraph;
import sim.core.Device;

import java.util.*;

public class CLIController {

    private final DeviceRegistry deviceRegistry;
    private final RouteRequestProducer routeRequestProducer;
    private final UndirectedGraph graph;
    private final Scanner scanner;
    private final TelemetryProducer telemetryProducer;

    public CLIController(DeviceRegistry deviceRegistry,
                         RouteRequestProducer routeRequestProducer,
                         UndirectedGraph graph, TelemetryProducer telemetryProducer) {
        this.deviceRegistry = deviceRegistry;
        this.routeRequestProducer = routeRequestProducer;
        this.graph = graph;
        this.scanner = new Scanner(System.in);
        this.telemetryProducer = telemetryProducer;
    }

    public void run() throws InterruptedException {
        System.out.println("Welcome to IoT Fleet Simulator");
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewDevices();
                    break;
                case 2:
                    moveDevice();
                    break;
                case 3:
                    viewActiveDevices();
                    break;
                case 4:
                    running = false;
                    System.out.println("Simulator finished.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        }
    }

    private void displayMenu() {
        // 1. View Devices
        System.out.println("1. View devices");
        // 2. Move Device
        System.out.println("2. Move device");
        // 3. View Active Devices
        System.out.println("3. View route");
        // 4. Exit
        System.out.println("4. Exit");
        // "Enter choice: "
        System.out.println("Select an option:");
    }

    private String engineOnOrOff(Boolean engineOn){
        if(engineOn){
            return "On";
        }
        else{
            return "Off";
        }
    }

    private void viewDevices() {
        Collection<Device> devices = deviceRegistry.getAllDevices();

        if(devices.isEmpty()) {
            System.out.println("No devices found.");
        }

        else {
            for (Device device : devices) {
                System.out.println("Device ID: " + device.getId()
                        + ", Type: " + device.getDeviceType()
                        + ", At city: " + getCityName(device.getCurrentNodeId())
                        + ", the engine is " + engineOnOrOff(device.getEngineOn())
                        + ", the fuel level is " + device.getFuelLevel());
            }
        }
    }

    private void viewActiveDevices() {
        Collection<Device> devices = deviceRegistry.getAllDevices();

        if(devices.isEmpty()) {
            System.out.println("No active devices found.");
        }

        else {
            for (Device device : devices) {
                if(device.getState() == DeviceState.InRoute)
                System.out.println("Device ID: " + device.getId()
                        + ", Type: " + device.getDeviceType()
                        + ", Progress: " + (int)(device.getProgressOnEdge() * 100) + "%"
                        + ", the engine is " + engineOnOrOff(device.getEngineOn())
                        + ", route: " + device.getPlannedRoute());
            }
        }
    }

    private void moveDevice() throws InterruptedException {
        viewDevices();

        System.out.print("Enter device ID: ");
        int deviceId = scanner.nextInt();
        scanner.nextLine();

        Device device = deviceRegistry.getDeviceById((long) deviceId);

        if(device == null) {
            System.out.println("Invalid device ID.");
            return;
        }
        if (device.getState() == DeviceState.InRoute){
            System.out.print("This device is already in route. Wait until it's arrived.");
            return;
        }

        System.out.println("Device is at: " + getCityName(device.getCurrentNodeId()));
        displayAvailableCities();

        System.out.print("Enter destination node ID (0-6): ");
        int destination = scanner.nextInt();
        scanner.nextLine();

        if(destination < 0 || destination > 6) {
            System.out.println("Invalid destination node.");
            return;
        }
        device.setPlannedRoute(null);
        routeRequestProducer.sendRouteRequest(deviceId, device.getCurrentNodeId(), destination);

        System.out.println("Route request sent!");
        try{
            int waitCount = 0;
            while (device.getPlannedRoute() == null && waitCount < 60) {
                Thread.sleep(500);
                waitCount++;
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("Interrupted while waiting for route.");
            return;
        }
        if (device.getPlannedRoute() == null) {
            System.out.println("Timeout: No route received from Route Service");
            return;
        }

        Thread executeThread = new Thread(() -> {
            try {
                executeRoute(device, device.getPlannedRoute());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Route execution interrupted for device " + device.getId());
            }
        });
        executeThread.start();
    }

    private void displayAvailableCities() {
        for (Map.Entry<Integer, UndirectedNode> entry : graph.getCities().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " +  entry.getValue().getLocationName()) ;

        }
    }

    private String getCityName(int nodeId) {
        return graph.getCityName(nodeId);
    }


    private void executeRoute(Device device, List<Integer> route) throws InterruptedException {
        System.out.println("Starting route: " + route);

        //setting device state and engine on
        device.setState(DeviceState.InRoute);
        device.setEngineOn(true);

        // Loop through each leg of the journey
        for (int i = 0; i < route.size() - 1; i++) {
            int fromNode = route.get(i);
            int toNode = route.get(i + 1);


            System.out.print("Route from " + getCityName(fromNode) + " to " + getCityName(toNode) + ": ");

            //gettign the distance between cities
            Double edgeDistance = graph.getEdgeWeight(fromNode, toNode);
            System.out.println("Distance is: " + edgeDistance);

            //resetting progress for this edge
            device.setProgressOnEdge(0.0);

            //simulate traveling along this edge
            while (device.getProgressOnEdge() < 1.0 && device.getFuelLevel() > 0) {

                //same logic as movement engine
                int minChange = 10;
                int maxChange = 20;
                Random random = new Random();
                double speedChangeVariable = random.nextDouble(maxChange - minChange + 1) + minChange;
                double speedPercentage = random.nextBoolean() ? -speedChangeVariable : speedChangeVariable;
                double percentToAdd = (speedPercentage/100) * maxSpeedOfVehicle(device.getDeviceType());

                 double speed = maxSpeedOfVehicle(device.getDeviceType()) + percentToAdd;
                 System.out.println("Speed: " + speed);
                 double kmPerSecond = speed / 3600;
                 double deltaSeconds = 200.0;  // 200 delta seconds per tick
                 double kmThisTick = kmPerSecond * deltaSeconds;
                 double progressIncrement = kmThisTick / edgeDistance;


                //updating progress
                 device.setProgressOnEdge(device.getProgressOnEdge() + progressIncrement);

                //movement engine logic for fuel
                 double fuelConsumed = kmThisTick / fuelConsumption(device.getDeviceType());
                 device.setFuelLevel(device.getFuelLevel() - fuelConsumed);
                 if (device.getFuelLevel() <= 0) {
                     device.setFuelLevel(0.0);
                     device.setState(DeviceState.EngineOff);
                     System.out.println("Out of fuel! Stopped at progress: " + device.getProgressOnEdge());
                     return;
                 }

                //printing progress
                 System.out.println("  Progress: " + (int)(device.getProgressOnEdge() * 100) + "%");
                 System.out.println("  Fuel level: " + device.getFuelLevel());
                 telemetryProducer.publishTelemetry(buildTelemetry(device, fromNode, speed));

                //sleeping for real like simulation
                 Thread.sleep(10000);
            }

            //arriving at next node
             device.setCurrentNodeId(toNode);
             device.setProgressOnEdge(0.0);
            System.out.println("Arrived at " + getCityName(toNode));
        }


         device.setState(DeviceState.Arrived);
         device.setPlannedRoute(null);
         System.out.println("Arrived at " + getCityName(device.getCurrentNodeId()));
        // Print "Journey complete!"
    }

    private DeviceTelemetryDTO buildTelemetry(Device device, Integer previousLocation, Double speed) {
        DeviceTelemetryDTO dto = DeviceTelemetryDTO.builder()
                .deviceId(device.getId())
                .deviceNumber(device.getDeviceNumber())
                .currentLocation(device.getCurrentNodeId())
                .previousLocation(previousLocation)
                .speed(speed)
                .engineOn(device.getEngineOn())
                .engineTemp(device.getEngineTemp())
                .fuelLevel(device.getFuelLevel())
                .timestamp(System.currentTimeMillis())
                .build();
        return dto;
    }


    private Double fuelConsumption(DeviceType deviceType) {
        switch (deviceType) {
            default -> {
                return 1.0;
            }
            case Car -> {
                return 30.0;
            }
            case Van ->  {
                return 20.0;
            }
            case Plane ->   {
                return 10.0;
            }
            case Truck ->   {
                return 5.0;
            }
            case Motorcycle ->   {
                return 40.0;
            }
        }
    }

    private Double maxSpeedOfVehicle(DeviceType deviceType) {
        switch (deviceType) {
            default -> {
                return 45.0;
            }
            case Car -> {
                return 120.0;
            }
            case Van ->  {
                return 80.0;
            }
            case Plane ->   {
                return 550.0;
            }
            case Truck ->   {
                return 100.0;
            }
            case Motorcycle ->   {
                return 160.0;
            }
        }
    }
}