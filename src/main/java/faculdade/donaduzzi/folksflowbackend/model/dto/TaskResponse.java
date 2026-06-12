package faculdade.donaduzzi.folksflowbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.model.entities.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Integer taskId;
    private String title;
    private String description;
    private Integer statusId;
    private Integer priorityId;
    private LocalDate dueDate;
    private Integer position;
    private Double estimatedHours;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .statusId(task.getStatus().getStatusId())
                .priorityId(task.getPriority().getPriorityId())
                .dueDate(task.getDueDate())
                .position(task.getPosition())
                .estimatedHours(task.getEstimatedHours())
                .isActive(task.getIsActive())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
