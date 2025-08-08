package com.travelapp.availability_service.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.travelapp.availability_service.entities.StayAvailability;
import com.travelapp.availability_service.repositories.StayAvailabilityRepository;

@Service
public class StayAvailabilityService {
	@Autowired
	StayAvailabilityRepository stayAvailabilityRepository;

	public List<StayAvailability> findByKeyStayIdAndKeyDate(@RequestParam String stayId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return stayAvailabilityRepository.findByKeyStayIdAndKeyDateBetween(stayId,
                startDate, endDate);
	}
}
