package com.userservice.userservice.kafka;

import com.userservice.userservice.dto.RouteCommandDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteRequestProducer {

    private final KafkaTemplate<String, RouteCommandDTO> kafkaTemplate;
    private final String TOPIC = "route-requests";

    public void sendRouteRequest(RouteCommandDTO dto) {
        kafkaTemplate.send(TOPIC, dto.getDeviceNumber().toString(), dto);
    }
}
