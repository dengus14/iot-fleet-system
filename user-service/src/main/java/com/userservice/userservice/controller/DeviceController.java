package com.userservice.userservice.controller;


import com.userservice.userservice.dto.DeviceDTO;
import com.userservice.userservice.service.DeviceServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceServiceProxy deviceServiceProxy;

    @GetMapping("/{id}")
    public DeviceDTO getDevice(@PathVariable Long id){
        return deviceServiceProxy.getDevice(id);

    }

    @GetMapping
    public List<DeviceDTO> getAllDevices(){
        return deviceServiceProxy.getDevices();
    }

    @PostMapping("/{id}/move")
    public String moveDevice(@PathVariable Long id, @RequestBody DeviceDTO deviceDTO){

        deviceServiceProxy.getDevice(id);
        //todo

        return "Route Sent Successfully";
    }
}
