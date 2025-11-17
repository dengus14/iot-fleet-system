package dto;

import enums.DeviceType;
import enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequestDTO {

    // Device sensors
    private Double engineTemp;
    private Double speed;
    private Boolean engineOn;
    private Integer deviceNumber;
    private Double fuelLevel;

    // Device location characteristics
    private DeviceType deviceType;
    private Integer startLocation;
    private Integer currentLocation;
    private DeviceStatus deviceStatus;
}
