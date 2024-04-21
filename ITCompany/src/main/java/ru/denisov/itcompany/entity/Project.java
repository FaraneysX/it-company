package ru.denisov.itcompany.entity;

import java.time.LocalDate;

public record Project(Long id,
                      String name,
                      LocalDate startDate) {
}
