package ru.prokdo.webtaxifleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String surname;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private LocalDate hireDate;
    private boolean isFree = true;
    private String licenseNumber;
}
