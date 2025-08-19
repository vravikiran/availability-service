package com.travelapp.availability_service.repositories;

import java.time.LocalDate;
import java.util.List;

import com.travelapp.availability_service.entities.StayAvailabilityKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.travelapp.availability_service.entities.StayAvailability;

@Repository
public interface StayAvailabilityRepository extends CassandraRepository<StayAvailability, StayAvailabilityKey> {

    List<StayAvailability> findByKeyStayIdAndKeyDateBetween(Long stayId, LocalDate startDate, LocalDate endDate);

    List<StayAvailability> findByKeyStayIdAndKeyDateBetweenAndKeyRoomId(
            Long stayId, LocalDate startDate, LocalDate endDate, int roomId);
    List<StayAvailability> findByKeyStayIdAndKeyDateGreaterThanEqual(Long stayId, LocalDate date);
}
