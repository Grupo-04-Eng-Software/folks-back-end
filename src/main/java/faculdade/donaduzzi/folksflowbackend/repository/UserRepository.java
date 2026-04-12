package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import faculdade.donaduzzi.folksflowbackend.entities.User;

@Repository
public interface UserRepository extends BaseRepository<User, Integer>{

    @Query("SELECT u FROM User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findByEmailActive(@Param("email") String email);

}
