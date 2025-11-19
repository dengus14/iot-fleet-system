package com.iotfleet.routeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteCommandDTO {

    private String commandId;
    private Integer deviceNumber;
    private String commandType;
    private List<Integer> plannedRoute;
    private Long timestamp;


}
