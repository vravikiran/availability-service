package com.travelapp.availability_service.entities;

import java.time.LocalDate;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class AvailabilityKey {
	@PrimaryKeyColumn(name = "stay_id", type = PrimaryKeyType.PARTITIONED)
	private String stayId;

	@PrimaryKeyColumn(name = "room_id", type = PrimaryKeyType.CLUSTERED)
	private int roomId;

	@PrimaryKeyColumn(name = "date", type = PrimaryKeyType.CLUSTERED)
	private LocalDate date;
}
