package sim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sim.core.DeviceState;
import sim.core.DeviceType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConfigDTO {
    private Long deviceId;
    private Double speed;
    private Boolean engineOn;
    private Integer deviceNumber;
    private Double fuelLevel;


    private DeviceType deviceType;
    private Integer startLocation;
    private Integer currentLocation;
    private DeviceState deviceStatus;



}
