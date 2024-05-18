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
public class Task {
    private Long id;
    private Long projectId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
