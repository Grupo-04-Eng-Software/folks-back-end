package faculdade.donaduzzi.folksflowbackend.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import faculdade.donaduzzi.folksflowbackend.entities.Task;

@Repository
public interface TaskRepository extends BaseRepository<Task, Integer>{

    @Query("SELECT DISTINCT t FROM Task t " +
            "LEFT JOIN t.assignees a " +
            "WHERE a.userId = :userId" +
            "ORDER BY t.dueDate DESC, t.isFavorite DESC")
    List<Task> findAllTaskByUser(@Param("userId") Integer userId);

    @Query("SELECT DISTINCT t FROM Task t " +
            "LEFT JOIN t.assignees a " +
            "WHERE a.userId = :userId " +
            "AND t.isFavorite = true " +
            "ORDER BY t.dueDate DESC")
    List<Task> findAllFavoritesTaskByUser(@Param("userId") Integer userId);
}
