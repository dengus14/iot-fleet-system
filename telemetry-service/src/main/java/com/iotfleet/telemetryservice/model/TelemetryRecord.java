package com.iotfleet.telemetryservice.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long deviceId;
    @Column
    private Integer deviceNumber;
    @Column
    private Integer currentLocation;
    @Column
    private Integer previousLocation;
    @Column
    private Double engineTemp;
    @Column
    private Double speed;
    @Column
    private Double fuelLevel;
    @Column
    private Boolean engineOn;
    @Column
    private Long timestamp;

}
