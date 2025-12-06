package com.userservice.userservice.dto;

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
    private String requestId;
    private Integer deviceNumber;
    private String commandType;
    private List<Integer> plannedRoute;
    private Long timestamp;


}
