package com.iotfleet.routeservice.kafka;


import com.iotfleet.routeservice.dto.RouteCommandDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteCommandProducer {
    private final KafkaTemplate<String, RouteCommandDTO> kafkaTemplate;
    private static final String TOPIC = "route-commands";

    public void sendRouteCommand(RouteCommandDTO dto) {
        kafkaTemplate.send(TOPIC,
                dto.getRequestId(),
                dto);
        log.info("Published device update event for device ID: {}", dto.getRequestId());
    }
}
