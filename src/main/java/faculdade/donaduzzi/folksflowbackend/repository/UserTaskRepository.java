package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.stereotype.Repository;
import faculdade.donaduzzi.folksflowbackend.model.entities.UserTask;

import java.util.List;

@Repository
public interface UserTaskRepository extends BaseRepository<UserTask, UserTask.UserTaskId> {

    // Todos os vínculos (responsáveis e redator) de uma tarefa.
    List<UserTask> findByTaskTaskId(Integer taskId);
}
