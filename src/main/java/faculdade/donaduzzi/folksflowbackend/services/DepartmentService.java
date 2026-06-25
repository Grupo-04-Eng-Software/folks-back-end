package faculdade.donaduzzi.folksflowbackend.services;

import faculdade.donaduzzi.folksflowbackend.infra.exceptions.BusinessException;
import faculdade.donaduzzi.folksflowbackend.model.dto.DepartmentRequest;
import faculdade.donaduzzi.folksflowbackend.model.dto.DepartmentResponse;
import faculdade.donaduzzi.folksflowbackend.model.entities.Company;
import faculdade.donaduzzi.folksflowbackend.model.entities.Department;
import faculdade.donaduzzi.folksflowbackend.repository.CompanyRepository;
import faculdade.donaduzzi.folksflowbackend.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private static final String DEPARTMENT_NOT_FOUND = "Department not found";

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    public List<DepartmentResponse> findByCompany(Integer companyId) {
        return departmentRepository.findByCompanyId(companyId).stream()
                .map(DepartmentResponse::fromEntity)
                .toList();
    }

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found"));

        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setColor(request.getColor());
        department.setCompany(company);
        department.setIsActive(true);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());

        return DepartmentResponse.fromEntity(departmentRepository.save(department));
    }

    @Transactional
    public DepartmentResponse update(Integer id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(DEPARTMENT_NOT_FOUND));

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setColor(request.getColor());
        department.setUpdatedAt(LocalDateTime.now());

        return DepartmentResponse.fromEntity(departmentRepository.save(department));
    }

    @Transactional
    public void delete(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(DEPARTMENT_NOT_FOUND));
        department.setIsActive(false);
        department.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(department);
    }
}
