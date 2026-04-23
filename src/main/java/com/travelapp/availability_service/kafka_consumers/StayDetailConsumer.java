package com.travelapp.availability_service.kafka_consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelapp.availability_service.dto.StayDetailDto;
import com.travelapp.availability_service.services.StayAvailabilityPersistenceService;
import com.travelapp.availability_service.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class StayDetailConsumer {
    Logger logger = LoggerFactory.getLogger(StayDetailConsumer.class);
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Autowired
    StayAvailabilityPersistenceService stayAvailabilityPersistenceService;

    @KafkaListener(topics = Constants.STAY_DETAIL_TOPIC, groupId = "stay-detail-consumer-group")
    public void consumeStayDetailMessages(String message,@Header(name = Constants.OPERATION_HEADER) String dbOperation) {
        try {
            System.out.println(message);
            StayDetailDto stayDetailDto = objectMapper.readValue(message, StayDetailDto.class);
            if(dbOperation.equals(Constants.CREATE)) {
                stayAvailabilityPersistenceService.createAvailabilities(stayDetailDto);
            } else {
                stayAvailabilityPersistenceService.updateAvailabilities(stayDetailDto);
            }
        } catch (JsonProcessingException e) {
            logger.error("Exception occurred while processing the message :: {}",e.getMessage());
        }
        logger.info("Consumed message :: {}", message);
        logger.info("DB Operation :: {}",dbOperation);
    }


}
