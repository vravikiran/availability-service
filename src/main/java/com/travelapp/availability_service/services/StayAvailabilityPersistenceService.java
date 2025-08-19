package com.travelapp.availability_service.services;

import com.travelapp.availability_service.entities.RoomCount;
import com.travelapp.availability_service.entities.StayAvailability;
import com.travelapp.availability_service.entities.StayAvailabilityKey;
import com.travelapp.availability_service.entities.StayRoomMappingDto;
import com.travelapp.availability_service.repositories.StayAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StayAvailabilityPersistenceService {
    @Autowired
    StayAvailabilityRepository stayAvailabilityRepository;

    @Transactional
    public void processCity(LocalDate startDate, LocalDate endDate, List<StayRoomMappingDto> stays) {
        for (StayRoomMappingDto stay : stays) {
            createAvailabilitiesForStay(startDate, endDate, stay);
        }
    }

    public void createAvailabilitiesForStay(LocalDate startDate, LocalDate endDate, StayRoomMappingDto stay) {
        Long stayId = stay.getStayId();
        Set<RoomCount> rooms = stay.getRoomIdWithCount();
        Instant now = Instant.now();
        List<StayAvailability> stayAvailabilities = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (RoomCount room : rooms) {
                stayAvailabilities.add(new StayAvailability(new StayAvailabilityKey(stayId,date,room.getRoomId()),true, now,room.getNoOfRooms()));
            }
        }
        stayAvailabilityRepository.saveAll(stayAvailabilities);
    }
}
