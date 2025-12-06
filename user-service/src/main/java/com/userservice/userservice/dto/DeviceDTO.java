package com.userservice.userservice.dto;

import com.userservice.userservice.enums.DeviceStatus;
import com.userservice.userservice.enums.DeviceType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class DeviceDTO {
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
