package com.travelapp.availability_service.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import com.travelapp.availability_service.entities.StayAvailability;
import com.travelapp.availability_service.services.StayAvailabilityService;

@RestController
@RequestMapping("/stays/availability")
public class StayAvailabilityController {
	@Autowired
	StayAvailabilityService stayAvailabilityService;

	@GetMapping
	public ResponseEntity<List<StayAvailability>> fetchAvailByStayAndRoomDetails(@RequestParam Long stayId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
		List<StayAvailability> availabilities = stayAvailabilityService.findByKeyStayIdAndKeyDate(stayId, startDate,
				endDate);
		return ResponseEntity.ok(availabilities);
	}

    @PostMapping("/bulk/update")
    public ResponseEntity<HttpStatus> updateAvailabilityForStays(LocalDate startDate, LocalDate endDate) {
        stayAvailabilityService.updateBulkAvailability(startDate,endDate);
        return  ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }
}
