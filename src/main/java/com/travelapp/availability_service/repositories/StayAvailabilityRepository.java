package com.travelapp.availability_service.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.travelapp.availability_service.entities.AvailabilityKey;
import com.travelapp.availability_service.entities.StayAvailability;

@Repository
public interface StayAvailabilityRepository extends CassandraRepository<StayAvailability, AvailabilityKey> {
	/*List<StayAvailability> findByKeyStayIdAndKeyRoomIdAndKeyDateBetween(String stayId, int roomId, LocalDate from,
			LocalDate to);*/

	List<StayAvailability> findByKeyStayIdAndKeyDateBetween(String stayId, LocalDate from, LocalDate to);
}
