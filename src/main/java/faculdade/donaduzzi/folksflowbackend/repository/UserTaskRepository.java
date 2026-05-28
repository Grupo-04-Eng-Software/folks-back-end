package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.stereotype.Repository;
import faculdade.donaduzzi.folksflowbackend.model.entities.UserTask;

@Repository
public interface UserTaskRepository extends BaseRepository<UserTask, UserTask.UserTaskId> {
}
