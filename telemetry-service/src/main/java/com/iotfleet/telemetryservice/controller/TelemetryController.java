package com.iotfleet.telemetryservice.controller;

import com.iotfleet.telemetryservice.model.TelemetryRecord;
import com.iotfleet.telemetryservice.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryRepository telemetryRepository;

    @GetMapping("/device/{deviceNumber}/latest")
    public ResponseEntity<TelemetryRecord> getLatestTelemetry(@PathVariable Integer deviceNumber) {
        List<TelemetryRecord> records = telemetryRepository.findByDeviceNumberOrderByTimestampDesc(deviceNumber);

        if (records.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(records.get(0));
    }

    @GetMapping("/device/{deviceNumber}/history")
    public ResponseEntity<List<TelemetryRecord>> getTelemetryHistory(
            @PathVariable Integer deviceNumber,
            @RequestParam(defaultValue = "50") int limit) {

        List<TelemetryRecord> records = telemetryRepository.findByDeviceNumberOrderByTimestampDesc(deviceNumber);

        if (records.size() > limit) {
            records = records.subList(0, limit);
        }

        return ResponseEntity.ok(records);
    }

    @GetMapping
    public ResponseEntity<List<TelemetryRecord>> getAllTelemetry() {
        return ResponseEntity.ok(telemetryRepository.findAll());
    }
}