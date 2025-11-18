package com.iotfleet.telemetryservice.kafka;

import com.iotfleet.telemetryservice.dto.DeviceTelemetryDTO;
import com.iotfleet.telemetryservice.model.TelemetryRecord;
import com.iotfleet.telemetryservice.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryConsumer {
    private final TelemetryRepository telemetryRepository;

    @KafkaListener(topics = "device-events",
            groupId = "telemetry-service-group")
    public void telemetryConsumer(DeviceTelemetryDTO dto) {
        TelemetryRecord telemetryRecord = TelemetryRecord.builder()
                .deviceId(dto.getDeviceId())
                .deviceNumber(dto.getDeviceNumber())
                .timestamp(dto.getTimestamp())
                .currentLocation(dto.getCurrentLocation())
                .previousLocation(dto.getPreviousLocation())
                .engineOn(dto.getEngineOn())
                .engineTemp(dto.getEngineTemp())
                .fuelLevel(dto.getFuelLevel())
                .speed(dto.getSpeed())
                .build();
        telemetryRepository.save(telemetryRecord);
        log.info("Kafka consumer received telemetry record {}", telemetryRecord);
    }
}
