package com.iotfleet.routeservice.kafka;


import com.iotfleet.routeservice.dto.RouteCommandDTO;
import com.iotfleet.routeservice.dto.RouteRequestDTO;
import com.iotfleet.routeservice.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteRequestConsumer {

    private static final String TOPIC = "route-requests";
    private final RouteService routeService;
    private final RouteCommandProducer routeCommandProducer;

    @KafkaListener(topics = TOPIC, groupId = "route-service-group")
    public void routeRequestListener(RouteRequestDTO dto) {
        Integer devNumber = dto.getDeviceNumber();
        Integer currentLocation = dto.getCurrentLocation();
        Integer destination = dto.getDestination();

        List<Integer> path = routeService.dijkstraShortestPathUpdated(currentLocation, destination);

        if (path.isEmpty()) {
            log.warn("No path found for device {} from {} to {}", devNumber, currentLocation, destination);
            return;
        }

        RouteCommandDTO routeCommandDTO = RouteCommandDTO.builder()
                .deviceNumber(devNumber)
                .plannedRoute(path)
                .timestamp(System.currentTimeMillis())
                .requestId(dto.getRequestId())
                .commandType("EXECUTE_ROUTE")
                .build();
        log.info("Route request received for device {} from {} to {}, calculating path...",
                devNumber, currentLocation, destination);
        routeCommandProducer.sendRouteCommand(routeCommandDTO);
        log.info("Route response sent for request {} with path: {}",
                dto.getRequestId(), routeCommandDTO.getPlannedRoute());
    }
}
