package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.model.DTO.TagRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.TagResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Space;
import faculdade.donaduzzi.folksflowbackend.model.entities.Tag;
import faculdade.donaduzzi.folksflowbackend.model.entities.Task;
import faculdade.donaduzzi.folksflowbackend.repository.SpaceRepository;
import faculdade.donaduzzi.folksflowbackend.repository.TagRepository;
import faculdade.donaduzzi.folksflowbackend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final SpaceRepository spaceRepository;
    private final TaskRepository taskRepository;

    public List<TagResponse> findAllBySpace(Integer spaceId) {
        return tagRepository.findBySpaceSpaceId(spaceId)
                .stream()
                .map(TagResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TagResponse create(TagRequest request) {
        Space space = spaceRepository.findById(request.getSpaceId())
                .orElseThrow(() -> new RuntimeException("Space not found"));

        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setColor(request.getColor());
        tag.setSpace(space);

        return TagResponse.fromEntity(tagRepository.save(tag));
    }

    @Transactional
    public void associateWithTask(Integer taskId, Integer tagId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        task.getTags().add(tag);
        taskRepository.save(task);
    }
}
