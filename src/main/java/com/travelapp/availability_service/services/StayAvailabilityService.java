package com.travelapp.availability_service.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.travelapp.availability_service.entities.StayRoomMappingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.travelapp.availability_service.entities.StayAvailability;
import com.travelapp.availability_service.repositories.StayAvailabilityRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class StayAvailabilityService {
    @Autowired
    private StayAvailabilityRepository stayAvailabilityRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private StayAvailabilityPersistenceService stayAvailabilityPersistenceService;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public List<StayAvailability> findByKeyStayIdAndKeyDate(@RequestParam String stayId,
                                                            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return stayAvailabilityRepository.findByKeyStayIdAndKeyDateBetween(stayId,
                startDate, endDate);
    }

    public void updateBulkAvailability(LocalDate startDate, LocalDate endDate) {
        String uri = "http://localhost:8090/stay/group-by-city";
        ResponseEntity<Map<String, List<StayRoomMappingDto>>> response =
                restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Map<String, List<StayRoomMappingDto>>>() {
                        }
                );
        Map<String, List<StayRoomMappingDto>> staysByCity = response.getBody();
        if (staysByCity != null && !staysByCity.isEmpty()) {
            createAvailabilities(startDate, endDate, staysByCity);
        }
    }

    private void createAvailabilities(LocalDate startDate, LocalDate endDate, Map<String, List<StayRoomMappingDto>> staysByCity) {
        staysByCity.values().forEach(stays ->
                CompletableFuture.runAsync(() -> {
                    try {

                        stayAvailabilityPersistenceService.processCity(startDate, endDate, stays);

                    } catch (Exception e) {
                        System.out.println("Error processing city" + e.getMessage());
                    }
                }, executor)

        );
    }
}
