package com.travelapp.availability_service.kafka_consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelapp.availability_service.dto.StayDetailDto;
import com.travelapp.availability_service.entities.RoomCount;
import com.travelapp.availability_service.entities.StayAvailability;
import com.travelapp.availability_service.entities.StayRoomMappingDto;
import com.travelapp.availability_service.repositories.StayAvailabilityRepository;
import com.travelapp.availability_service.services.StayAvailabilityPersistenceService;
import com.travelapp.availability_service.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StayDetailConsumer {
    Logger logger = LoggerFactory.getLogger(StayDetailConsumer.class);
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Autowired
    StayAvailabilityPersistenceService stayAvailabilityPersistenceService;
    @Autowired
    StayAvailabilityRepository stayAvailabilityRepository;

    @KafkaListener(topics = Constants.STAY_DETAIL_TOPIC, groupId = "stay-detail-consumer-group")
    public void consumeStayDetailMessages(String message,@Header(name = Constants.OPERATION_HEADER, required = true) String dbOperation) {
        try {
            StayDetailDto stayDetailDto = objectMapper.readValue(message, StayDetailDto.class);
            if(dbOperation.equals(Constants.CREATE)) {
                createAvailabilities(stayDetailDto);
            } else {
                updateAvailabilities(stayDetailDto);
            }
        } catch (JsonProcessingException e) {
            logger.error("Exception occurred while processing the message :: {}",e.getMessage());
        }
        logger.info("Consumed message :: {}", message);
        logger.info("DB Operation :: {}",dbOperation);
    }

    private void createAvailabilities(StayDetailDto stayDetailDto) {
        Set<RoomCount> roomCountSet = stayDetailDto.getRooms().stream().map(roomDetailDto -> new RoomCount(roomDetailDto.getRoomId(),roomDetailDto.getNumberOfRooms())).collect(Collectors.toSet());
        StayRoomMappingDto stayRoomMappingDto = new StayRoomMappingDto(stayDetailDto.getId(),roomCountSet);
        stayAvailabilityPersistenceService.createAvailabilitiesForStay(LocalDate.now(),LocalDate.now().plusMonths(6),stayRoomMappingDto);
        logger.info("Create availabilities for stay with id ::{} ",stayDetailDto.getId());
    }

    private void updateAvailabilities(StayDetailDto stayDetailDto) {
        boolean isActive = stayDetailDto.isActive();
        if(!isActive) {
           List<StayAvailability> availabilities = stayAvailabilityRepository.findByKeyStayIdAndKeyDateGreaterThanEqual(stayDetailDto.getId(),LocalDate.now());
           stayAvailabilityRepository.deleteAll(availabilities);
        } else {
            createAvailabilities(stayDetailDto);
        }
    }
}
