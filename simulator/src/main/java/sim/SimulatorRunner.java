package sim;


import sim.backend.BackendClient;
import sim.backend.DeviceDTO;
import sim.config.BackendConfig;
import sim.core.Device;
import sim.core.DeviceFactory;
import sim.core.MovementEngine;
import sim.graph.UndirectedGraph;
import sim.registry.DeviceRegistry;

import java.util.List;


public class SimulatorRunner {
private DeviceRegistry registry;
private MovementEngine movementEngine;
    public void start() throws InterruptedException {
        BackendClient backendClient = new BackendClient(new BackendConfig());
        System.out.println("Backend configured");
        List<DeviceDTO> deviceDTOList = backendClient.fetchDevices();
        System.out.println("Devices fetched");

        System.out.println(deviceDTOList.get(0).toString());

        List<Device> deviceList = DeviceFactory.fromDTOs(deviceDTOList);
        UndirectedGraph graph = new UndirectedGraph(7);
        graph.createEdges();
        graph.printGraph();
        movementEngine = new MovementEngine(graph);

        registry = new DeviceRegistry();
        registry.loadInitialDevices(deviceList);

        runSimulationLoop();
    }

    private void runSimulationLoop() throws InterruptedException {
        while(true) {
            tick();
            Thread.sleep(100*30);
        }
    }
    private void tick(){
        for (Device device : registry.getAllDevices()) {
             movementEngine.update(device,3);

             System.out.println("Device " + device.getId()+ " at node " + device.getCurrentNodeId() +
                             ", going to: " + device.getNextNodeId() +
                             " fuel " + device.getFuelLevel() +
                             " speed " + device.getSpeed() +
                     " progress " + device.getProgressOnEdge()

             );
             ;

            // telemetryEngine.send(device);
        }
    }

    public static void main(String[] args) throws InterruptedException {

    new SimulatorRunner().start();
    }
}
//BackendClient backendClient = new BackendClient(new BackendConfig());
////        System.out.println("Backend configured");
////List<DeviceDTO> deviceDTOList = backendClient.fetchDevices();
////        System.out.println("Devices fetched");
////List<Device> deviceList = DeviceFactory.fromDTOs(deviceDTOList);
////
////
////DeviceRegistry registry = new DeviceRegistry();
////        registry.loadInitialDevices(deviceList);
////
////        while(true){
////
////                Thread.sleep(100); // 100ms = 10 ticks per second
////
////        }