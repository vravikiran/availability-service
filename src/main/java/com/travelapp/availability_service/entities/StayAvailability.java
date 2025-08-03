package com.travelapp.availability_service.entities;

import java.time.Instant;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("stay_availability")
public class StayAvailability {
	@PrimaryKey
	AvailabilityKey key;

	@Column("is_available")
	private boolean isAvailable;

	@Column("last_updated")
	private Instant lastUpdated;

	@Column("no_of_rooms")
	private int noOfRooms;
}
