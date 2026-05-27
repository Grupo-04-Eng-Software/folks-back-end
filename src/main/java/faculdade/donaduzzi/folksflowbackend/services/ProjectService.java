package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.model.DTO.ProjectRequest;
import faculdade.donaduzzi.folksflowbackend.model.DTO.ProjectResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Project;
import faculdade.donaduzzi.folksflowbackend.model.entities.Space;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.entities.UserProject;
import faculdade.donaduzzi.folksflowbackend.repository.ProjectRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SpaceService spaceService;
    private final UserProjectRepository userProjectRepository;

    public List<ProjectResponse> findAllBySpace(Integer spaceId) {
        return projectRepository.findBySpaceSpaceId(spaceId)
                .stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request, User user) {
        Space space = spaceService.findById(request.getSpaceId());

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setSpace(space);
        project.setIsActive(true);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        Project savedProject = projectRepository.save(project);

        UserProject userProject = new UserProject();
        userProject.setId(new UserProject.UserProjectId(user.getUserId(), savedProject.getProjectId()));
        userProject.setProject(savedProject);
        userProject.setUser(user);
        userProject.setRole("OWNER");
        userProjectRepository.save(userProject);

        return ProjectResponse.fromEntity(savedProject);
    }

    @Transactional
    public void delete(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        projectRepository.delete(project);
    }
}
