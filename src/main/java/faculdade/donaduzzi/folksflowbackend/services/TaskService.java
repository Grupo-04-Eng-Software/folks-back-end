package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.model.DTO.TaskRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TaskResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Priority;
import faculdade.donaduzzi.folksflowbackend.model.entities.Status;
import faculdade.donaduzzi.folksflowbackend.model.entities.Task;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.entities.UserTask;
import faculdade.donaduzzi.folksflowbackend.repository.PriorityRepository;
import faculdade.donaduzzi.folksflowbackend.repository.TaskRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserTaskRepository;
import faculdade.donaduzzi.folksflowbackend.repository.ChecklistItemRepository;
import faculdade.donaduzzi.folksflowbackend.repository.TimeEntryRepository;
import faculdade.donaduzzi.folksflowbackend.repository.specifications.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final StatusService statusService;
    private final PriorityRepository priorityRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final TimeEntryRepository timeEntryRepository;

    public List<TaskResponse> findOverdue() {
        return taskRepository.findOverdueTasks(LocalDate.now())
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> findAllByStatus(Integer statusId) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getStatus().getStatusId().equals(statusId))
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Task findById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional
    public TaskResponse create(TaskRequest request, User author) {
        Status status = statusService.findById(request.getStatusId());
        Priority priority = priorityRepository.findById(request.getPriorityId())
                .orElseThrow(() -> new RuntimeException("Priority not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(request.getDueDate());
        task.setEstimatedHours(request.getEstimatedHours());
        task.setIsActive(true);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        if (request.getPosition() == null) {
            task.setPosition(0);
        } else {
            task.setPosition(request.getPosition());
        }

        if (request.getParentTaskId() != null) {
            Task parent = findById(request.getParentTaskId());
            task.setParentTask(parent);
        }

        Task savedTask = taskRepository.save(task);

        // Criar relacionamento de REDATOR
        UserTask userTask = new UserTask();
        userTask.setId(new UserTask.UserTaskId(savedTask.getTaskId(), author.getUserId()));
        userTask.setTask(savedTask);
        userTask.setUser(author);
        userTask.setRole("REDATOR");
        userTask.setIsFavorite(false);
        userTask.setCreatedAt(LocalDateTime.now());
        userTaskRepository.save(userTask);

        return TaskResponse.fromEntity(savedTask);
    }

    @Transactional
    public TaskResponse moveTask(Integer taskId, Integer targetStatusId, Integer newPosition) {
        Task task = findById(taskId);
        Status targetStatus = statusService.findById(targetStatusId);

        task.setStatus(targetStatus);
        task.setPosition(newPosition);
        task.setUpdatedAt(LocalDateTime.now());

        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    @Transactional
    public void assignUser(Integer taskId, Integer userId) {
        Task task = findById(taskId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserTask userTask = new UserTask();
        userTask.setId(new UserTask.UserTaskId(taskId, userId));
        userTask.setTask(task);
        userTask.setUser(user);
        userTask.setRole("ASSIGNEE");
        userTask.setIsFavorite(false);
        userTask.setCreatedAt(LocalDateTime.now());

        userTaskRepository.save(userTask);
    }

    @Transactional
    public void unassignUser(Integer taskId, Integer userId) {
        UserTask.UserTaskId id = new UserTask.UserTaskId(taskId, userId);
        userTaskRepository.deleteById(id);
    }

    @Transactional
    public void delete(Integer id) {
        Task task = findById(id);
        task.setIsActive(false);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Transactional
    public void addChecklistItem(Integer taskId, String content) {
        Task task = findById(taskId);
        faculdade.donaduzzi.folksflowbackend.model.entities.ChecklistItem item = new faculdade.donaduzzi.folksflowbackend.model.entities.ChecklistItem();
        item.setTask(task);
        item.setContent(content);
        item.setIsCompleted(false);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        checklistItemRepository.save(item);
    }

    @Transactional
    public void toggleChecklistItem(Integer itemId) {
        faculdade.donaduzzi.folksflowbackend.model.entities.ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Checklist item not found"));
        item.setIsCompleted(!item.getIsCompleted());
        item.setUpdatedAt(LocalDateTime.now());
        checklistItemRepository.save(item);
    }

    @Transactional
    public void startTime(Integer taskId, User user) {
        Task task = findById(taskId);
        timeEntryRepository.findByTaskAndUserAndEndTimeIsNull(task, user)
                .ifPresent(te -> { throw new RuntimeException("Timer already running for this task and user"); });

        faculdade.donaduzzi.folksflowbackend.model.entities.TimeEntry entry = new faculdade.donaduzzi.folksflowbackend.model.entities.TimeEntry();
        entry.setTask(task);
        entry.setUser(user);
        entry.setStartTime(LocalDateTime.now());
        timeEntryRepository.save(entry);
    }

    @Transactional
    public void stopTime(Integer taskId, User user) {
        Task task = findById(taskId);
        faculdade.donaduzzi.folksflowbackend.model.entities.TimeEntry entry = timeEntryRepository.findByTaskAndUserAndEndTimeIsNull(task, user)
                .orElseThrow(() -> new RuntimeException("No running timer found for this task and user"));

        entry.setEndTime(LocalDateTime.now());
        long minutes = java.time.Duration.between(entry.getStartTime(), entry.getEndTime()).toMinutes();
        entry.setDurationMinutes(minutes);
        timeEntryRepository.save(entry);
    }

    public Long getTotalTimeSpent(Integer taskId) {
        return timeEntryRepository.findByTaskTaskId(taskId).stream()
                .mapToLong(te -> te.getDurationMinutes() != null ? te.getDurationMinutes() : 0L)
                .sum();
    }

    public Page<TaskResponse> searchTasks(Integer statusId, Integer priorityId, Integer tagId, String title, Pageable pageable) {
        Specification<Task> spec = Specification.where(TaskSpecifications.hasStatus(statusId))
                .and(TaskSpecifications.hasPriority(priorityId))
                .and(TaskSpecifications.hasTag(tagId))
                .and(TaskSpecifications.titleContains(title));

        return taskRepository.findAll(spec, pageable).map(TaskResponse::fromEntity);
    }
}
