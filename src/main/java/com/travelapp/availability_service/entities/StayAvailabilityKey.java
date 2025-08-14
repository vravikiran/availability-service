package com.travelapp.availability_service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyClass
public class StayAvailabilityKey implements Serializable {
    @PrimaryKeyColumn(name = "stay_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long stayId;

    @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private LocalDate date;

    @PrimaryKeyColumn(name = "room_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private int roomId;

}
