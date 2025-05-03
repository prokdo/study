package ru.prokdo.webtaxifleet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id", unique = true, nullable = false)
    private Integer driverId;

    @Column(name = "car_id", unique = true, nullable = false)
    private Integer carId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "start_location", nullable = false, length = 255)
    private String startLocation;

    @Column(name = "end_location", length = 255)
    private String endLocation;
}
