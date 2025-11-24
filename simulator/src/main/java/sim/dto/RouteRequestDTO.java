package sim.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteRequestDTO {

    private Integer destination;
    private Integer deviceNumber;
    private Integer currentLocation;
    private String requestId;
    private Long timestamp;
}
