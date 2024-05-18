package ru.denisov.itcompany.mapper;

import ru.denisov.itcompany.dto.project.controller.ProjectControllerDto;
import ru.denisov.itcompany.dto.project.view.ProjectViewDto;
import ru.denisov.itcompany.entity.Project;

public class ProjectMapper {
    public Project map(ProjectControllerDto obj) {
        return Project.builder()
                .id(obj.id())
                .name(obj.name())
                .startDate(obj.startDate())
                .build();
    }

    public ProjectControllerDto map(Project obj) {
        return ProjectControllerDto.builder()
                .id(obj.id())
                .name(obj.name())
                .startDate(obj.startDate())
                .build();
    }

    public ProjectViewDto mapController(ProjectControllerDto obj) {
        return ProjectViewDto.builder()
                .id(obj.id().toString())
                .name(obj.name())
                .startDate(obj.startDate().toString())
                .build();
    }
}
