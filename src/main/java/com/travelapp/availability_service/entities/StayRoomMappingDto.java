package com.travelapp.availability_service.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StayRoomMappingDto {
    private Long stayId;
    private Set<RoomCount> roomIdWithCount;
}
