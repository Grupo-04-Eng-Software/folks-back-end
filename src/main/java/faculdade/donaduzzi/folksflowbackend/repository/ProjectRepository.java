package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.stereotype.Repository;
import faculdade.donaduzzi.folksflowbackend.model.entities.Project;
import java.util.List;

@Repository
public interface ProjectRepository extends BaseRepository<Project, Integer> {
    
    List<Project> findBySpaceSpaceId(Integer spaceId);
}
