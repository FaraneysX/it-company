package ru.denisov.itcompany.mapper;

import ru.denisov.itcompany.dto.project.controller.ProjectControllerDto;
import ru.denisov.itcompany.dto.project.view.ProjectViewDto;
import ru.denisov.itcompany.entity.Project;

public class ProjectMapper {
    public Project mapToEntity(ProjectControllerDto obj) {
        return Project.builder()
                .id(obj.id())
                .name(obj.name())
                .startDate(obj.startDate())
                .build();
    }

    public ProjectControllerDto mapToController(Project obj) {
        return ProjectControllerDto.builder()
                .id(obj.id())
                .name(obj.name())
                .startDate(obj.startDate())
                .build();
    }

    public ProjectViewDto mapToView(ProjectControllerDto obj) {
        return ProjectViewDto.builder()
                .id(obj.id().toString())
                .name(obj.name())
                .startDate(obj.startDate().toString())
                .build();
    }
}
