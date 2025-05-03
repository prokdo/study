package ru.prokdo.webtaxifleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String surname;
    private String phone;
    private String email;
    private String passwordHash;
}
