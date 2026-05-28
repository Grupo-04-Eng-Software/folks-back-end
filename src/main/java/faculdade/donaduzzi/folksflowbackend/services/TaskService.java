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
import lombok.RequiredArgsConstructor;
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
        task.setIsActive(true);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        if (request.getPosition() == null) {
            task.setPosition(0);
        } else {
            task.setPosition(request.getPosition());
        }

        if (request.getParentTaskId() != null) {
            Task parent = taskRepository.findById(request.getParentTaskId())
                    .orElseThrow(() -> new RuntimeException("Parent task not found"));
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
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        Status targetStatus = statusService.findById(targetStatusId);

        task.setStatus(targetStatus);
        task.setPosition(newPosition);
        task.setUpdatedAt(LocalDateTime.now());

        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    @Transactional
    public void assignUser(Integer taskId, Integer userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
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
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setIsActive(false);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }
}
