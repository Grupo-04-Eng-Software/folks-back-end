package faculdade.donaduzzi.folksflowbackend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faculdade.donaduzzi.folksflowbackend.model.entities.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Integer taskId;
    private String title;
    private String description;
    private Integer statusId;
    private String statusName;
    private String statusColor;
    private Boolean isFinalStatus;
    private Integer projectId;
    private String projectName;
    private Integer spaceId;
    private String spaceName;
    private Integer priorityId;
    private String priorityName;
    private LocalDate dueDate;
    private Integer position;
    private Double estimatedHours;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AssigneeInfo> assignees;
    private List<TagInfo> tags;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssigneeInfo {
        private Integer userId;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagInfo {
        private Integer tagId;
        private String name;
        private String color;
    }

    public static TaskResponse fromEntity(Task task) {
        var status = task.getStatus();
        var project = status.getProject();
        var space = project.getSpace();

        List<AssigneeInfo> assigneeList = task.getAssignees() == null ? List.of() :
            task.getAssignees().stream()
                .map(ut -> new AssigneeInfo(ut.getUser().getUserId(), ut.getUser().getName()))
                .collect(Collectors.toList());

        List<TagInfo> tagList = task.getTags() == null ? List.of() :
            task.getTags().stream()
                .map(t -> new TagInfo(t.getTagId(), t.getName(), t.getColor()))
                .collect(Collectors.toList());

        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .statusId(status.getStatusId())
                .statusName(status.getName())
                .statusColor(status.getColor())
                .isFinalStatus(status.getIsFinalStatus())
                .projectId(project.getProjectId())
                .projectName(project.getName())
                .spaceId(space.getSpaceId())
                .spaceName(space.getName())
                .priorityId(task.getPriority().getPriorityId())
                .priorityName(task.getPriority().getName())
                .dueDate(task.getDueDate())
                .position(task.getPosition())
                .estimatedHours(task.getEstimatedHours())
                .isActive(task.getIsActive())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .assignees(assigneeList)
                .tags(tagList)
                .build();
    }
}
