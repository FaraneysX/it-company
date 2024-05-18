package ru.denisov.itcompany.mapper;

import ru.denisov.itcompany.dto.task.controller.TaskControllerDto;
import ru.denisov.itcompany.dto.task.view.TaskViewDto;
import ru.denisov.itcompany.entity.Task;

public class TaskMapper {
    public Task mapToEntity(TaskControllerDto obj) {
        return Task.builder()
                .id(obj.id())
                .projectId(obj.projectId())
                .name(obj.name())
                .startDate(obj.startDate())
                .endDate(obj.endDate())
                .build();
    }

    public TaskControllerDto mapToController(Task obj) {
        return TaskControllerDto.builder()
                .id(obj.id())
                .projectId(obj.projectId())
                .name(obj.name())
                .startDate(obj.startDate())
                .endDate(obj.endDate())
                .build();
    }

    public TaskViewDto mapToView(TaskControllerDto obj) {
        return TaskViewDto.builder()
                .id(obj.id().toString())
                .projectId(obj.projectId().toString())
                .name(obj.name())
                .startDate(obj.startDate().toString())
                .endDate(obj.endDate().toString())
                .build();
    }
}
