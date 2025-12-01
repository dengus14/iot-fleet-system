package com.iotfleet.deviceservice.dto;


import com.iotfleet.deviceservice.enums.DeviceStatus;
import com.iotfleet.deviceservice.enums.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConfigDTO {
    private Long deviceId;
    private Double speed;
    private Boolean engineOn;
    private Integer deviceNumber;
    private Double fuelLevel;


    private DeviceType deviceType;
    private Integer startLocation;
    private Integer currentLocation;
    private DeviceStatus deviceStatus;


}
