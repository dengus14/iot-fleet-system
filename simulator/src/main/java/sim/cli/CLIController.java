package sim.cli;

import sim.core.DeviceState;
import sim.graph.UndirectedNode;
import sim.registry.DeviceRegistry;
import sim.kafka.RouteRequestProducer;
import sim.graph.UndirectedGraph;
import sim.core.Device;

import java.util.Collection;
import java.util.List;
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

    private void moveDevice() throws InterruptedException {
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
        device.setPlannedRoute(null);
        routeRequestProducer.sendRouteRequest(deviceNumber, device.getCurrentNodeId(), destination);

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
        executeRoute(device, device.getPlannedRoute());
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
                 double speed = device.getSpeed();
                 System.out.println("Speed: " + speed);
                 double kmPerSecond = speed / 3600;
                 double deltaSeconds = 200.0;  // 1 second per tick
                 double kmThisTick = kmPerSecond * deltaSeconds;
                 double progressIncrement = kmThisTick / edgeDistance;

                //updating progress
                 device.setProgressOnEdge(device.getProgressOnEdge() + progressIncrement);

                //movement engine logic for fuel
                 device.setFuelLevel(device.getFuelLevel() - speed * 0.0500);
                 if (device.getFuelLevel() <= 0) {
                     device.setFuelLevel(0.0);
                     device.setState(DeviceState.EngineOff);
                     System.out.println("Out of fuel! Stopped at progress: " + device.getProgressOnEdge());
                     return;
                 }

                //printing progress
                 System.out.println("  Progress: " + (int)(device.getProgressOnEdge() * 100) + "%");
                 System.out.println("  Fuel level: " + device.getFuelLevel());

                //sleeping for real like simulation
                 Thread.sleep(1000);
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
}