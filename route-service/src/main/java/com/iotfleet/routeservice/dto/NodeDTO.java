package com.iotfleet.routeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeDTO {
    private Integer id;
    private String name;
    private Double x;  // x coordinate for map
    private Double y;  // y coordinate for map
}