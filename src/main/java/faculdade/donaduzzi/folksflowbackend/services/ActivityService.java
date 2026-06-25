package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;

import faculdade.donaduzzi.folksflowbackend.model.dto.ActivityRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.ActivityResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Activity;
import faculdade.donaduzzi.folksflowbackend.model.entities.Task;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.entities.UserTask;
import faculdade.donaduzzi.folksflowbackend.model.enums.NotificationType;
import faculdade.donaduzzi.folksflowbackend.repository.ActivityRepository;
import faculdade.donaduzzi.folksflowbackend.repository.TaskRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;
    private final UserTaskRepository userTaskRepository;
    private final NotificationService notificationService;

    public List<ActivityResponse> findAllByTask(Integer taskId) {
        // Ordem cronológica (mais antigo primeiro) para o chat de comentários.
        return activityRepository.findByTaskTaskIdOrderByCreatedAtAsc(taskId)
                .stream()
                .map(ActivityResponse::fromEntity)
                .toList();
    }

    @Transactional
    public ActivityResponse create(ActivityRequest request, User user) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new BusinessException("Task not found"));

        Activity activity = new Activity();
        activity.setContent(request.getContent());
        activity.setTask(task);
        activity.setUser(user);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        Activity saved = activityRepository.save(activity);

        // Notifica os participantes da tarefa (responsáveis/redator), exceto o autor.
        for (UserTask ut : userTaskRepository.findByTaskTaskId(task.getTaskId())) {
            User participant = ut.getUser();
            if (participant != null && !participant.getUserId().equals(user.getUserId())) {
                notificationService.sendNotification(
                        participant,
                        user.getName() + " comentou na tarefa: " + task.getTitle(),
                        NotificationType.NEW_COMMENT,
                        "/tasks/" + task.getTaskId()
                );
            }
        }

        return ActivityResponse.fromEntity(saved);
    }
}
