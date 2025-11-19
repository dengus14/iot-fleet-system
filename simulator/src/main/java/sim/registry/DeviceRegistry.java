package sim.registry;

import sim.core.Device;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceRegistry {
    private final Map<Long, Device> devices = new HashMap<>();

    public void loadInitialDevices(Collection<Device> devices){
        this.devices.clear();
        for(Device d:devices){
            this.devices.put(d.getId(),d);
        }


    }
    public void addOrUpdateDevice(Device device){
        devices.put(device.getId(), device);
    }
    public boolean removeDevice(Long id){
        return devices.remove(id) != null;
    }
    public Device getDevice(Long id){
        return devices.get(id);
    }
    public Collection<Device> getAllDevices(){
            return devices.values();
    }


}
