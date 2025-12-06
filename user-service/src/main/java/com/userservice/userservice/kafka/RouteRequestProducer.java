package com.userservice.userservice.kafka;

import com.userservice.userservice.dto.RouteCommandDTO;
import com.userservice.userservice.dto.RouteRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteRequestProducer {

    private final KafkaTemplate<String, RouteRequestDTO> kafkaTemplate;
    private final String TOPIC = "route-requests";

    public void sendRouteRequest(RouteRequestDTO dto) {
        kafkaTemplate.send(TOPIC, dto.getDeviceNumber().toString(), dto);
        log.info("Send route request {}", dto);
    }
}
