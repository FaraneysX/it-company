package ru.denisov.itcompany.mapper;

import ru.denisov.itcompany.dto.taskparticipation.controller.TaskParticipationControllerDto;
import ru.denisov.itcompany.dto.taskparticipation.view.TaskParticipationViewDto;
import ru.denisov.itcompany.entity.TaskParticipation;

public class TaskParticipationMapper {
    public TaskParticipation map(TaskParticipationControllerDto obj) {
        return TaskParticipation.builder()
                .id(obj.id())
                .taskId(obj.taskId())
                .employeeId(obj.employeeId())
                .build();
    }

    public TaskParticipationControllerDto map(TaskParticipation obj) {
        return TaskParticipationControllerDto.builder()
                .id(obj.id())
                .taskId(obj.taskId())
                .employeeId(obj.employeeId())
                .build();
    }

    public TaskParticipationViewDto mapController(TaskParticipationControllerDto obj) {
        return TaskParticipationViewDto.builder()
                .id(obj.id().toString())
                .taskId(obj.taskId().toString())
                .employeeId(obj.employeeId().toString())
                .build();
    }
}
