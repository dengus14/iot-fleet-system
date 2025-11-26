package sim.cli;

import sim.graph.UndirectedNode;
import sim.registry.DeviceRegistry;
import sim.kafka.RouteRequestProducer;
import sim.graph.UndirectedGraph;
import sim.core.Device;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class CLIController {

    private final DeviceRegistry deviceRegistry;
    private final RouteRequestProducer routeRequestProducer;
    private final UndirectedGraph graph;
    private final Scanner scanner;

    public CLIController(DeviceRegistry deviceRegistry,
                         RouteRequestProducer routeRequestProducer,
                         UndirectedGraph graph) {
        this.deviceRegistry = deviceRegistry;
        this.routeRequestProducer = routeRequestProducer;
        this.graph = graph;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
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
        // 3. Exit
        System.out.println("3. Exit");
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
                System.out.println("Device Number: " + device.getDeviceNumber()
                        + ", Type: " + device.getDeviceType()
                        + ", At city: " + getCityName(device.getCurrentNodeId())
                        + ", the engine is " + engineOnOrOff(device.getEngineOn())
                        + ", the fuel level is " + device.getFuelLevel());
            }
        }
    }

    private void moveDevice() {
        viewDevices();

        System.out.print("Enter device number: ");
        int deviceNumber = scanner.nextInt();
        scanner.nextLine();

        Device device = deviceRegistry.getDevice((long) deviceNumber);

        if(device == null) {
            System.out.println("Invalid device number.");
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

        routeRequestProducer.sendRouteRequest(deviceNumber, device.getCurrentNodeId(), destination);

        System.out.println("Route request sent!");
    }

    private void displayAvailableCities() {
        for (Map.Entry<Integer, UndirectedNode> entry : graph.getCities().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " +  entry.getValue().getLocationName()) ;

        }
    }

    private String getCityName(int nodeId) {
        return graph.getCityName(nodeId);
    }
}