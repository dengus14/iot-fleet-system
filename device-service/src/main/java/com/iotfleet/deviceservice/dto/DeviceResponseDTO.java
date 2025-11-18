package com.iotfleet.deviceservice.dto;

import com.iotfleet.deviceservice.enums.DeviceType;
import com.iotfleet.deviceservice.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponseDTO {

    private Long id;

    // Device sensors
    private Double engineTemp;
    private Double speed;
    private Boolean engineOn;
    private Integer deviceNumber;
    private Double fuelLevel;

    // Device location characteristics
    private DeviceType deviceType;
    private Integer startLocation;
    private Integer currentLocation;
    private DeviceStatus deviceStatus;
}
