package service;
import dto.DeviceRequestDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import model.Device;
import model.UndirectedGraph;
import org.springframework.stereotype.Service;
import repository.DeviceRepository;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;



    /**
     * @param deviceRequestDTO - the device to create
     * @return the saved device with generated ID
     */
    public String createDevice(DeviceRequestDTO deviceRequestDTO) {
        Device device = Device.builder()
                .engineTemp(deviceRequestDTO.getEngineTemp())
                .speed(deviceRequestDTO.getSpeed())
                .engineOn(deviceRequestDTO.getEngineOn())
                .fuelLevel(deviceRequestDTO.getFuelLevel())
                .deviceNumber(deviceRequestDTO.getDeviceNumber())
                .startLocation(deviceRequestDTO.getStartLocation())
                .currentLocation(deviceRequestDTO.getCurrentLocation())
                .deviceType(deviceRequestDTO.getDeviceType())
                .deviceStatus(deviceRequestDTO.getDeviceStatus())
                .build();

        deviceRepository.save(device);
        return device.toString();
    }

    /**
     * @param deviceRequestDTO - device ID
     * @return the device, or throw exception if not found
     */
    public String getDeviceById(DeviceRequestDTO deviceRequestDTO) {
        Device device = deviceRepository.findByDeviceNumber(deviceRequestDTO.getDeviceNumber());
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        return device.toString();
    }

    /**
     * @return list of all devices
     */
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }



    /**
     * @param deviceRequestDTO - which device to update
     * @param engineTemp - new engine temperature
     * @param speed - new speed
     * @param fuelLevel - new fuel level
     * @return updated device
     */
    public String updateDeviceSensors(DeviceRequestDTO deviceRequestDTO, Double engineTemp, Double speed, Double fuelLevel) {
        Device device = deviceRepository.findByDeviceNumber(deviceRequestDTO.getDeviceNumber());
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        device.setEngineTemp(engineTemp);
        device.setSpeed(speed);
        device.setFuelLevel(fuelLevel);
        deviceRepository.save(device);
        return device.toString();
    }

    /**
     * @param deviceRequestDTO - which device
     * @param engineOn - true to turn on, false to turn off
     * @return updated device
     */
    public Device updateEngineStatus(DeviceRequestDTO deviceRequestDTO, Boolean engineOn) {
        Device device = deviceRepository.findByDeviceNumber(deviceRequestDTO.getDeviceNumber());
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        device.setEngineOn(engineOn);
        deviceRepository.save(device);
        return null;
    }

    /**
     * @param locationId - the node/location ID
     * @return list of devices at that location
     */
    public List<Device> getDevicesByCurrentLocation(Integer locationId) {
        return deviceRepository.findAllByCurrentLocation(locationId);
    }

    /**
     * @param deviceRequestDTO - device ID to delete
     */
    public void deleteDevice(DeviceRequestDTO deviceRequestDTO) {
        Device device = deviceRepository.findByDeviceNumber(deviceRequestDTO.getDeviceNumber());
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        deviceRepository.deleteByDeviceNumber(deviceRequestDTO.getDeviceNumber());
    }


}
