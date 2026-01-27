package com.iotfleet.deviceservice.model;

import com.iotfleet.deviceservice.enums.DeviceStatus;
import com.iotfleet.deviceservice.enums.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "device")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //device sensors
    @Column
    private Double engineTemp;
    @Column
    private Double speed;
    @Column
    private Boolean engineOn;
    @Column
    private Integer deviceNumber;
    @Column
    private Double fuelLevel;

    @Column
    private Double progressOnEdge;
    @Column
    private Integer nextNodeId;
    @ElementCollection
    @CollectionTable(name = "device_planned_route", joinColumns = @JoinColumn(name = "device_id"))
    @Column(name = "node_id")
    private List<Integer> plannedRoute;


    //device location characteristics
    @Column(nullable = false)
    private DeviceType deviceType;
    @Column(nullable = false)
    private Integer startLocation;
    @Column(nullable = false)
    private Integer currentLocation;
    @Column(nullable = false)
    private DeviceStatus deviceStatus;


}
