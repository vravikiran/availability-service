package com.travelapp.availability_service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCount {
    private int roomId;
    private int noOfRooms;
}
