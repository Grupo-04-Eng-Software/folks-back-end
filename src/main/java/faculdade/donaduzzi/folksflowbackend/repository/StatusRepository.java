package faculdade.donaduzzi.folksflowbackend.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import faculdade.donaduzzi.folksflowbackend.entities.Status;

@Repository
public interface StatusRepository extends BaseRepository<Status, Integer>{

    @Query ("SELECT s FROM Status s " +
            "WHERE s.project.projectId = :projectId " +
            "ORDER BY s.position")
    List<Status> findByProject(@Param("projectId") Integer projectId);
}
