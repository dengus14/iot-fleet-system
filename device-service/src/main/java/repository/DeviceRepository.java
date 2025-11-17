package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import model.Device;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    List<Device> findAllByEngineTemp(Double engineTemp);
    List<Device> findAll();
    Device findByDeviceNumber(Integer deviceId);
    List<Device> findAllByCurrentLocation(Integer locationId);
    Device deleteByDeviceNumber(Integer deviceNumber);
}
