package com.iotfleet.deviceservice.dto;

import com.iotfleet.deviceservice.model.Device;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTelemetryDTO {

    private Long deviceId;
    private Integer deviceNumber;
    private Integer currentLocation;
    private Integer previousLocation;
    private Double engineTemp;
    private Double speed;
    private Boolean engineOn;
    private Double fuelLevel;
    private Long timestamp;


    public static DeviceTelemetryDTO getDeviceTelemetryDTO(Device device) {
        DeviceTelemetryDTO deviceTelemetryDTO = new DeviceTelemetryDTO();
        deviceTelemetryDTO.setDeviceId(device.getId());
        deviceTelemetryDTO.setDeviceNumber(device.getDeviceNumber());
        deviceTelemetryDTO.setEngineTemp(device.getEngineTemp());
        deviceTelemetryDTO.setFuelLevel(device.getFuelLevel());
        deviceTelemetryDTO.setSpeed(device.getSpeed());
        deviceTelemetryDTO.setTimestamp(Long.valueOf(System.currentTimeMillis()));
        deviceTelemetryDTO.setEngineOn(device.getEngineOn());
        deviceTelemetryDTO.setCurrentLocation(device.getCurrentLocation());
        deviceTelemetryDTO.setPreviousLocation(device.getCurrentLocation());
        return deviceTelemetryDTO;
    }
}
