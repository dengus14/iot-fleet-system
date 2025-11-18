package com.iotfleet.deviceservice.dto;

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
}
