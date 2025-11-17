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
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;



    /**
     * @param deviceRequestDTO - the device to create
     * @return the saved device with generated ID
     */
    public Device createDevice(DeviceRequestDTO deviceRequestDTO) {
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
        return device;
    }

    /**
     * @param deviceNumber - device ID
     * @return the device, or throw exception if not found
     */
    public Device getDeviceByNumber(Integer deviceNumber) {
        Device device = deviceRepository.findByDeviceNumber(deviceNumber);
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        return device;
    }

    /**
     * @return list of all devices
     */
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }



    /**
     * @param deviceNumber - which device to update
     * @param engineTemp - new engine temperature
     * @param speed - new speed
     * @param fuelLevel - new fuel level
     * @return updated device
     */
    public Device updateDeviceSensors(Integer deviceNumber, Double engineTemp, Double speed, Double fuelLevel) {
        Device device = deviceRepository.findByDeviceNumber(deviceNumber);
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        device.setEngineTemp(engineTemp);
        device.setSpeed(speed);
        device.setFuelLevel(fuelLevel);
        deviceRepository.save(device);
        return device;
    }

    /**
     * @param deviceNumber - which device
     * @param engineOn - true to turn on, false to turn off
     * @return updated device
     */
    public Device updateEngineStatus(Integer deviceNumber, Boolean engineOn) {
        Device device = deviceRepository.findByDeviceNumber(deviceNumber);
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        device.setEngineOn(engineOn);
        deviceRepository.save(device);
        return device;
    }

    /**
     * @param locationId - the node/location ID
     * @return list of devices at that location
     */
    public List<Device> getDevicesByCurrentLocation(Integer locationId) {
        return deviceRepository.findAllByCurrentLocation(locationId);
    }

    /**
     * @param deviceNumber - device ID to delete
     */
    public void deleteDevice(Integer deviceNumber) {
        Device device = deviceRepository.findByDeviceNumber(deviceNumber);
        if (device == null) {
            throw new RuntimeException("Device not found");
        }
        deviceRepository.deleteByDeviceNumber(deviceNumber);
    }


}
