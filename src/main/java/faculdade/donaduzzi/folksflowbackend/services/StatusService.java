package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;

import faculdade.donaduzzi.folksflowbackend.model.dto.StatusRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.StatusResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Project;
import faculdade.donaduzzi.folksflowbackend.model.entities.Status;
import faculdade.donaduzzi.folksflowbackend.repository.ProjectRepository;
import faculdade.donaduzzi.folksflowbackend.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;
    private final ProjectRepository projectRepository;

    public List<StatusResponse> findAllByProject(Integer projectId) {
        return statusRepository.findByProject(projectId)
                .stream()
                .map(StatusResponse::fromEntity)
                .toList();
    }

    @Transactional
    public StatusResponse create(StatusRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new BusinessException("Project not found"));

        Status status = new Status();
        status.setName(request.getName());
        status.setColor(request.getColor());
        status.setProject(project);
        status.setIsFinalStatus(request.getIsFinalStatus() != null && request.getIsFinalStatus());
        
        // Se a posição não for enviada, coloca no final
        if (request.getPosition() == null) {
            List<Status> existingStatuses = statusRepository.findByProject(project.getProjectId());
            status.setPosition(existingStatuses.size());
        } else {
            status.setPosition(request.getPosition());
        }

        status.setCreatedAt(LocalDateTime.now());
        status.setUpdatedAt(LocalDateTime.now());

        return StatusResponse.fromEntity(statusRepository.save(status));
    }

    @Transactional
    public void delete(Integer id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Status not found"));
        statusRepository.delete(status);
    }

    public Status findById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Status not found"));
    }
}
