package com.iotfleet.telemetryservice.repository;


import com.iotfleet.telemetryservice.model.TelemetryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelemetryRepository extends JpaRepository<TelemetryRecord, Long> {
    List<TelemetryRecord> findByDeviceNumber(Integer deviceNumber);
}
