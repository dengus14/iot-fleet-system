package sim.core;

import sim.backend.DeviceDTO;

import java.util.ArrayList;
import java.util.List;

public class DeviceFactory {

    public static List<Device> fromDTOs(List<DeviceDTO> dtos){
        List<Device> devices= new ArrayList<>();
        for(DeviceDTO d:dtos ) {
        Device device = new Device();
            device.setId(d.getId());
            device.setEngineTemp(d.getEngineTemp());
            device.setSpeed(d.getSpeed());
            device.setEngineOn(d.getEngineOn());
            device.setDeviceNumber(d.getDeviceNumber());
            device.setFuelLevel(d.getFuelLevel());
            device.setCurrentNodeId(d.getCurrentLocation());
            //more fields if needd


            devices.add(device);
        }

        return devices;
    }

}
