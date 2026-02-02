package com.userservice.userservice.controller;


import com.userservice.userservice.dto.DeviceDTO;
import com.userservice.userservice.dto.DeviceRequestDTO;
import com.userservice.userservice.dto.MoveRequest;
import com.userservice.userservice.dto.RouteRequestDTO;
import com.userservice.userservice.kafka.RouteRequestProducer;
import com.userservice.userservice.service.DeviceServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceServiceProxy deviceServiceProxy;
    private final RouteRequestProducer routeRequestProducer;

    @GetMapping("/{id}")
    public DeviceDTO getDevice(@PathVariable Long id){
        return deviceServiceProxy.getDevice(id);

    }

    @GetMapping
    public List<DeviceDTO> getAllDevices(){
        return deviceServiceProxy.getDevices();
    }

    @PostMapping
    public DeviceDTO createDevice(@RequestBody DeviceRequestDTO deviceDTO){
        return deviceServiceProxy.createDevice(deviceDTO);
    }

    @PostMapping("/{id}/move")
    public String moveDevice(
            @PathVariable Long id,
            @RequestParam Integer destination) {  // Changed from @RequestBody

        DeviceDTO device = deviceServiceProxy.getDevice(id);

        RouteRequestDTO routeRequestDTO = RouteRequestDTO.builder()
                .requestId(UUID.randomUUID().toString())
                .deviceNumber(id.intValue())
                .currentLocation(device.getCurrentLocation())
                .destination(destination)  // Use directly
                .build();
        routeRequestProducer.sendRouteRequest(routeRequestDTO);
        return "Route Sent Successfully";
    }
}
