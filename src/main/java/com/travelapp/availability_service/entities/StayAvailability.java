package com.travelapp.availability_service.entities;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("stay_availability")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StayAvailability {
    @PrimaryKey
    private StayAvailabilityKey key;
    @Column("is_available")
    private Boolean isAvailable = true;
    @Column("last_updated")
    private Instant lastUpdated;
    @Column("no_of_rooms")
    private Integer noOfRooms;
}
