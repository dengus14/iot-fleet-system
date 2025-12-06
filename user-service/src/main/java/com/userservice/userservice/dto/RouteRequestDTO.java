
package com.userservice.userservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequestDTO {
    private String requestId;
    private Integer deviceNumber;
    private Integer currentLocation;
    private Integer destination;
}
