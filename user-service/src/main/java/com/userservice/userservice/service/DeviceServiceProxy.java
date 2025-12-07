package com.userservice.userservice.service;

import com.userservice.userservice.dto.DeviceDTO;
import com.userservice.userservice.dto.DeviceRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor

public class DeviceServiceProxy {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String deviceServiceUrl= "http://localhost:8081/api/devices";

    public List<DeviceDTO> getDevices(){
        ResponseEntity<DeviceDTO[]> response = restTemplate.getForEntity(deviceServiceUrl, DeviceDTO[].class);


    return   Arrays.asList(response.getBody());
    }

    public DeviceDTO getDevice(Long deviceId){
        return restTemplate.getForEntity(deviceServiceUrl+"/"+deviceId, DeviceDTO.class).getBody();


    }

    public DeviceDTO createDevice(DeviceRequestDTO deviceDTO){

        return restTemplate.postForEntity(deviceServiceUrl, deviceDTO, DeviceDTO.class).getBody();
    }

}
