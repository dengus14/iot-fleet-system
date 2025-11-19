package sim;

import lombok.extern.slf4j.Slf4j;
import sim.backend.BackendClient;
import sim.backend.DeviceDTO;
import sim.config.BackendConfig;
import sim.core.Device;
import sim.core.DeviceFactory;
import sim.registry.DeviceRegistry;

import java.util.List;

@Slf4j
public class SimulatorRunner {
private DeviceRegistry registry;
    public void start() throws InterruptedException {
        BackendClient backendClient = new BackendClient(new BackendConfig());
        System.out.println("Backend configured");
        List<DeviceDTO> deviceDTOList = backendClient.fetchDevices();
        System.out.println("Devices fetched");
        List<Device> deviceList = DeviceFactory.fromDTOs(deviceDTOList);


        registry = new DeviceRegistry();
        registry.loadInitialDevices(deviceList);
        runSimulationLoop();
    }

    private void runSimulationLoop() throws InterruptedException {
        while(true) {
            tick();
            Thread.sleep(100);
        }
    }
    private void tick(){
        for (Device device : registry.getAllDevices()) {
            // movementEngine.update(device);
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