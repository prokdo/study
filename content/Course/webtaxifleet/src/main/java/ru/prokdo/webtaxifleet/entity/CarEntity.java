package ru.prokdo.webtaxifleet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "make", nullable = false, length = 50)
    private String make;

    @Column(name = "model", nullable = false, length = 50)
    private String model;

    @Column(name = "license_plate", unique = true, nullable = false, length = 15)
    private String licensePlate;

    @Column(name = "driver_id", unique = true)
    private Integer driverId;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
}
