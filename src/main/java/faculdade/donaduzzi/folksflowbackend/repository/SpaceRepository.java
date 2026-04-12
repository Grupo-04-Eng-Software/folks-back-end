package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import faculdade.donaduzzi.folksflowbackend.entities.Space;


@Repository
public interface SpaceRepository extends BaseRepository<Space, Integer>{

    @Query("SELECT DISTINCT s FROM Space s " +
            "LEFT JOIN s.members m " +
            "WHERE m.user.userId = :userId " +
            "AND s.isActive = true " +
            "ORDER BY s.createdAt DESC")
    List<Space> findAllSpacesByUserId(@Param("userId") Integer userId);

}
