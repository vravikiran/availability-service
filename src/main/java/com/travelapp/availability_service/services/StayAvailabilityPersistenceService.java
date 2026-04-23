package com.travelapp.availability_service.services;

import com.travelapp.availability_service.dto.StayDetailDto;
import com.travelapp.availability_service.entities.RoomCount;
import com.travelapp.availability_service.entities.StayAvailability;
import com.travelapp.availability_service.entities.StayAvailabilityKey;
import com.travelapp.availability_service.entities.StayRoomMappingDto;
import com.travelapp.availability_service.repositories.StayAvailabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StayAvailabilityPersistenceService {
    @Autowired
    StayAvailabilityRepository stayAvailabilityRepository;
    Logger logger = LoggerFactory.getLogger(StayAvailabilityPersistenceService.class);

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
                stayAvailabilities.add(new StayAvailability(new StayAvailabilityKey(stayId, date, room.getRoomId()), true, now, room.getNoOfRooms()));
            }
        }
        stayAvailabilityRepository.saveAll(stayAvailabilities);
    }

    @Async
    public void createAvailabilities(StayDetailDto stayDetailDto) {
        Set<RoomCount> roomCountSet = stayDetailDto.getRooms().stream().map(roomDetailDto -> new RoomCount(roomDetailDto.getRoomId(), roomDetailDto.getNumberOfRooms())).collect(Collectors.toSet());
        StayRoomMappingDto stayRoomMappingDto = new StayRoomMappingDto(stayDetailDto.getId(), roomCountSet);
        createAvailabilitiesForStay(LocalDate.now(), LocalDate.now().plusMonths(6), stayRoomMappingDto);
        logger.info("Creating availabilities for stay with id ::{} ", stayDetailDto.getId());
    }

    @Async
    public void updateAvailabilities(StayDetailDto stayDetailDto) {
        boolean isActive = stayDetailDto.isActive();
        if (!isActive) {
            List<StayAvailability> availabilities = stayAvailabilityRepository.findByKeyStayIdAndKeyDateGreaterThanEqual(stayDetailDto.getId(), LocalDate.now());
            List<StayAvailability> updatedAvailabilities = availabilities.stream().map(stayAvailability -> {
                stayAvailability.setIsAvailable(false);
                return stayAvailability;
            }).toList();
            stayAvailabilityRepository.saveAll(updatedAvailabilities);
        } else {
            createAvailabilities(stayDetailDto);
        }
    }
}
