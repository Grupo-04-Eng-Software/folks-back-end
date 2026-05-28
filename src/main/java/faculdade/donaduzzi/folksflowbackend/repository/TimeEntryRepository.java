package faculdade.donaduzzi.folksflowbackend.repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.TimeEntry;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.entities.Task;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeEntryRepository extends BaseRepository<TimeEntry, Integer> {
    Optional<TimeEntry> findByTaskAndUserAndEndTimeIsNull(Task task, User user);
    List<TimeEntry> findByTaskTaskId(Integer taskId);
}
