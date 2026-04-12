package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import faculdade.donaduzzi.folksflowbackend.entities.Space;
import faculdade.donaduzzi.folksflowbackend.entities.User;

@Repository
public interface SpaceRepository extends BaseRepository<Space, Integer>{

    @Query("SELECT DISTINCT s FROM Space s " +
            "LEFT JOIN s.members m " +
            "WHERE (s.creator.userId = :userId " +
            "OR m.userId = :userId) " +
            "AND s.isActive = true")
    List<Space> findAllProjectsByUserId(@Param("userId") Integer userId);

    @Query("SELECT s FROM Space s " +
            "WHERE s.creator.userId = :userId " +
            "AND s.isActive = true")
    List<Space> findAllByCreatorId(@Param("userId") Integer userId);

    @Query("SELECT s from Space s " +
            "WHERE s.spaceId = :spaceId " +
            "AND s.isActive = true")
    Optional<Space> findActiveBySpaceId(@Param("spaceID") Integer spaceId);


}
