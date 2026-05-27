package faculdade.donaduzzi.folksflowbackend.repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends BaseRepository<Activity, Integer> {
    List<Activity> findByTaskTaskIdOrderByCreatedAtDesc(Integer taskId);
}
