package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;

import faculdade.donaduzzi.folksflowbackend.model.dto.ActivityRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.ActivityResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Activity;
import faculdade.donaduzzi.folksflowbackend.model.entities.Task;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.repository.ActivityRepository;
import faculdade.donaduzzi.folksflowbackend.repository.TaskRepository;
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

    public List<ActivityResponse> findAllByTask(Integer taskId) {
        return activityRepository.findByTaskTaskIdOrderByCreatedAtDesc(taskId)
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

        return ActivityResponse.fromEntity(activityRepository.save(activity));
    }
}
