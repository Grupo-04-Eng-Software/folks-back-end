package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<E, ID> extends JpaRepository<E, ID> {

    @Query ("SELECT e FROM #{entityName} e Where e.isActive = true")
    List<E> findAllActive();

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.isActive = true")
    Optional<E> findByIdActive(@Param("id") ID id);

    @Query("UPDATE #{#entityName} e SET e.isActive = false WHERE e.id = :id")
    void softDelete(@Param("id") ID id);

    @Query("UPDATE #{#entityName} e SET e.isActive = true WHERE e.id = :id")
    void reactivate(@Param("id") ID id);
}
