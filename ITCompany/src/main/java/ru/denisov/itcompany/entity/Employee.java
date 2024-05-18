package ru.denisov.itcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Employee {
    private Long id;
    private Long projectId;
    private Long positionId;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private String password;
    private Role role;
}
