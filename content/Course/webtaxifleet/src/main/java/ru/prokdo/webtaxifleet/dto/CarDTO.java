package ru.prokdo.webtaxifleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    private Integer id;
    private String make;
    private String model;
    private String licensePlate;
    private Integer driverId;
    private LocalDate registrationDate;
}
