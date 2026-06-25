package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;

import faculdade.donaduzzi.folksflowbackend.model.dto.CandidateResponse;
import faculdade.donaduzzi.folksflowbackend.model.dto.CompanyResponse;
import faculdade.donaduzzi.folksflowbackend.model.dto.ProjectRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.ProjectResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Candidate;
import faculdade.donaduzzi.folksflowbackend.model.entities.Company;
import faculdade.donaduzzi.folksflowbackend.model.entities.Project;
import faculdade.donaduzzi.folksflowbackend.model.entities.Space;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.entities.UserProject;
import faculdade.donaduzzi.folksflowbackend.model.entities.Status;
import faculdade.donaduzzi.folksflowbackend.repository.CandidateRepository;
import faculdade.donaduzzi.folksflowbackend.repository.CompanyRepository;
import faculdade.donaduzzi.folksflowbackend.repository.ProjectRepository;
import faculdade.donaduzzi.folksflowbackend.repository.StatusRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SpaceService spaceService;
    private final UserProjectRepository userProjectRepository;
    private final StatusRepository statusRepository;
    private final CompanyRepository companyRepository;
    private final CandidateRepository candidateRepository;

    public List<ProjectResponse> findAllBySpace(Integer spaceId) {
        return projectRepository.findBySpaceSpaceId(spaceId)
                .stream()
                .map(ProjectResponse::fromEntity)
                .toList();
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

        // Criar Status Padrão
        createDefaultStatus("Backlog", "#7f8c8d", 0, savedProject, false);
        createDefaultStatus("A Fazer", "#3498db", 1, savedProject, false);
        createDefaultStatus("Em Andamento", "#f1c40f", 2, savedProject, false);
        createDefaultStatus("Concluído", "#2ecc71", 3, savedProject, true);

        return ProjectResponse.fromEntity(savedProject);
    }

    private void createDefaultStatus(String name, String color, Integer position, Project project, Boolean isFinal) {
        Status status = new Status();
        status.setName(name);
        status.setColor(color);
        status.setPosition(position);
        status.setProject(project);
        status.setIsFinalStatus(isFinal);
        status.setCreatedAt(LocalDateTime.now());
        status.setUpdatedAt(LocalDateTime.now());
        statusRepository.save(status);
    }

    @Transactional
    public ProjectResponse update(Integer id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Project not found"));
        
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setUpdatedAt(LocalDateTime.now());
        
        // Espaço não costuma mudar em edições simples, mas se o request trouxer um novo spaceId, podemos atualizar
        if (request.getSpaceId() != null && !request.getSpaceId().equals(project.getSpace().getSpaceId())) {
            Space space = spaceService.findById(request.getSpaceId());
            project.setSpace(space);
        }

        Project updatedProject = projectRepository.save(project);
        return ProjectResponse.fromEntity(updatedProject);
    }

    @Transactional
    public void delete(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Project not found"));
        project.setIsActive(false);
        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);
    }

    // ── Vínculo Projeto ↔ Empresa ──────────────────────────────────────────────

    private Project getProject(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException("Project not found"));
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> listCompanies(Integer projectId) {
        return getProject(projectId).getCompanies().stream()
                .map(CompanyResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void associateCompany(Integer projectId, Integer companyId) {
        Project project = getProject(projectId);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Company not found"));
        project.getCompanies().add(company);
        projectRepository.save(project);
    }

    @Transactional
    public void disassociateCompany(Integer projectId, Integer companyId) {
        Project project = getProject(projectId);
        project.getCompanies().removeIf(c -> c.getCompanyId().equals(companyId));
        projectRepository.save(project);
    }

    // ── Vínculo Projeto ↔ Candidato ────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<CandidateResponse> listCandidates(Integer projectId) {
        return getProject(projectId).getCandidates().stream()
                .map(CandidateResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void associateCandidate(Integer projectId, Integer candidateId) {
        Project project = getProject(projectId);
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new BusinessException("Candidate not found"));
        project.getCandidates().add(candidate);
        projectRepository.save(project);
    }

    @Transactional
    public void disassociateCandidate(Integer projectId, Integer candidateId) {
        Project project = getProject(projectId);
        project.getCandidates().removeIf(c -> c.getCandidateId().equals(candidateId));
        projectRepository.save(project);
    }
}
