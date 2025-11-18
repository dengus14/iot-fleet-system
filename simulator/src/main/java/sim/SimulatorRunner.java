package sim;

import sim.backend.BackendClient;
import sim.backend.DeviceDTO;
import sim.config.BackendConfig;

import java.util.List;

public class SimulatorRunner {
    public static void main(String[] args) {
        BackendClient backendClient = new BackendClient(new BackendConfig());
        System.out.println("Backend configured");
        List<DeviceDTO> deviceDTOList = backendClient.fetchDevices();
        System.out.println("Devices fetched");
        for (DeviceDTO dto : deviceDTOList) {
            System.out.println(dto);
        }
    }
}
