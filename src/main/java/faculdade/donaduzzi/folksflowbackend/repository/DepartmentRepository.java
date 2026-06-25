package faculdade.donaduzzi.folksflowbackend.repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends BaseRepository<Department, Integer> {

    @Query("SELECT d FROM Department d " +
            "WHERE d.company.companyId = :companyId " +
            "ORDER BY d.createdAt")
    List<Department> findByCompanyId(@Param("companyId") Integer companyId);
}
