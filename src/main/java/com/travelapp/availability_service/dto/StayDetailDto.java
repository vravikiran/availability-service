package com.travelapp.availability_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StayDetailDto {
    private Long id;
    private Set<RoomDetailDto> rooms;
    private boolean active;
    private boolean approved;
}
