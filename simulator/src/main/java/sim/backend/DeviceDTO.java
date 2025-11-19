package sim.backend;


import lombok.Data;

@Data
public class DeviceDTO {



    private Long id;
    private Double engineTemp;
    private Double speed;
    private String deviceType;

    private Boolean engineOn;
    private Integer deviceNumber;
    private Double fuelLevel;

    private Long startLocation;
    private Long currentLocation;
    private String deviceStatus;
}
