package com.iotfleet.deviceservice.controller;

import com.iotfleet.deviceservice.dto.DeviceRequestDTO;
import lombok.RequiredArgsConstructor;
import com.iotfleet.deviceservice.model.Device;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.iotfleet.deviceservice.service.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * POST /api/devices
     * @param deviceRequestDTO - device data from request body
     * @return ResponseEntity with created device and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody DeviceRequestDTO deviceRequestDTO) throws Exception {
        Device device = deviceService.createDevice(deviceRequestDTO);
        return ResponseEntity.ok(device);
    }

    /**
     * GET /api/devices/{deviceNumber}
     * @param deviceNumber - device number from URL path
     * @return ResponseEntity with device and HTTP 200 status
     */
    @GetMapping("/{deviceNumber}")
    public ResponseEntity<Device> getDeviceByNumber(@PathVariable Integer deviceNumber) {
        Device device = deviceService.getDeviceByNumber(deviceNumber);
        return ResponseEntity.ok(device);
    }

    /**
     * GET /api/devices
     * @return ResponseEntity with list of all devices and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        return new ResponseEntity<>(deviceService.getAllDevices(), HttpStatus.OK);
    }

    /**
     * GET /api/devices/location/{locationId}
     * @param locationId - current location ID
     * @return ResponseEntity with list of devices at that location
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Device>> getDevicesByLocation(@PathVariable Integer locationId) {
        List<Device> devices = deviceService.getDevicesByCurrentLocation(locationId);
        return ResponseEntity.ok(devices);
    }

    /**
     * PUT /api/devices/{deviceNumber}/sensors
     * @param deviceNumber - device number from URL
     * @param engineTemp - new engine temperature
     * @param speed - new speed
     * @param fuelLevel - new fuel level
     * @return ResponseEntity with updated device
     */
    @PutMapping("/{deviceNumber}/sensors")
    public ResponseEntity<Device> updateDeviceSensors(
            @PathVariable Integer deviceNumber,
            @RequestParam Double engineTemp,
            @RequestParam Double speed,
            @RequestParam Double fuelLevel) {
        Device device = deviceService.updateDeviceSensors(deviceNumber, engineTemp, speed, fuelLevel);
        return ResponseEntity.ok(device);
    }

    /**
     * PUT /api/devices/{deviceNumber}/engine
     * @param deviceNumber - device number from URL
     * @param engineOn - true to turn on, false to turn off
     * @return ResponseEntity with updated device
     */
    @PutMapping("/{deviceNumber}/engine")
    public ResponseEntity<Device> updateEngineStatus(
            @PathVariable Integer deviceNumber,
            @RequestParam Boolean engineOn) {
        Device device = deviceService.updateEngineStatus(deviceNumber, engineOn);
        return ResponseEntity.ok(device);
    }

    /**
     * DELETE /api/devices/{deviceNumber}
     * @param deviceNumber - device number to delete
     * @return ResponseEntity with HTTP 204 NO_CONTENT status
     */
    @DeleteMapping("/{deviceNumber}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Integer deviceNumber) {
        deviceService.deleteDevice(deviceNumber);
        return ResponseEntity.noContent().build();
    }
}
