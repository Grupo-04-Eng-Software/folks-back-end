package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.model.DTO.TaskRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TaskResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Priority;
import faculdade.donaduzzi.folksflowbackend.model.entities.Status;
import faculdade.donaduzzi.folksflowbackend.model.entities.Task;
import faculdade.donaduzzi.folksflowbackend.repository.PriorityRepository;
import faculdade.donaduzzi.folksflowbackend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final StatusService statusService;
    private final PriorityRepository priorityRepository;

    public List<TaskResponse> findAllByStatus(Integer statusId) {
        Status status = statusService.findById(statusId);
        // Precisamos de um método no repositório para buscar por status
        // Por enquanto, vamos filtrar manualmente ou criar o método
        return taskRepository.findAll().stream()
                .filter(t -> t.getStatus().getStatusId().equals(statusId))
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponse create(TaskRequest request) {
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
            task.setPosition(0); // Lógica simples inicial
        } else {
            task.setPosition(request.getPosition());
        }

        if (request.getParentTaskId() != null) {
            Task parent = taskRepository.findById(request.getParentTaskId())
                    .orElseThrow(() -> new RuntimeException("Parent task not found"));
            task.setParentTask(parent);
        }

        return TaskResponse.fromEntity(taskRepository.save(task));
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
    public void delete(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
