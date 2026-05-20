package faculdade.donaduzzi.folksflowbackend.repository;

<<<<<<< HEAD
import org.springframework.stereotype.Repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.Project;

@Repository
public interface ProjectRepository extends BaseRepository<Project, Integer> {
}

=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import faculdade.donaduzzi.folksflowbackend.entities.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends BaseRepository<Project, Integer> {
    /*@Query("SELECT p FROM Project" +
            "JOIN UserProject up" +
            "WHERE p.projectId = up.projectId" +
            "JOIN user u" +
            "WHERE up.userId = u.userId" +
            "ORDER BY p.createdAt DESC"
    )*/
    List<Project> findAllProjectsByUserId(Integer userId);

    /*@Query(
            "SELECT p FROM Project" +
                    "JOIN UserSpace us" +
                    "WHERE p.spaceId = us.spaceId"
    )*/
    List<Project> findAllProjectsBySpaceId(Integer spaceId);
}
>>>>>>> JwtConfig
