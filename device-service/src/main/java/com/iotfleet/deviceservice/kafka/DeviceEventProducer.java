package com.iotfleet.deviceservice.kafka;


import com.iotfleet.deviceservice.dto.DeviceTelemetryDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceEventProducer {
    private final KafkaTemplate<String, DeviceTelemetryDTO> kafkaTemplate;
    private static final String TOPIC = "device-events";

    public void publishDeviceUpdate(DeviceTelemetryDTO deviceTelemetryDTO) {
        kafkaTemplate.send(TOPIC,
                deviceTelemetryDTO.getDeviceId().toString(),
                deviceTelemetryDTO);
        log.info("Published device update event for device ID: {}", deviceTelemetryDTO.getDeviceId());
    }
}
