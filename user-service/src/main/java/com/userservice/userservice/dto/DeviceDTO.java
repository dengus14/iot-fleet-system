package com.userservice.userservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class DeviceDTO {
    private Long id;

    //device sensors

    private Double engineTemp;

    private Double speed;

    private Boolean engineOn;

    private Integer deviceNumber;

    private Double fuelLevel;
}
