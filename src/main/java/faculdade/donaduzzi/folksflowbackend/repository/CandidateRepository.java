package faculdade.donaduzzi.folksflowbackend.repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.Candidate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends BaseRepository<Candidate, Integer> {
    
    @Query("SELECT c FROM Candidate c JOIN c.companies comp WHERE comp.companyId = :companyId")
    List<Candidate> findByCompanyId(@Param("companyId") Integer companyId);
    
    List<Candidate> findByNameContainingIgnoreCase(String name);
}
