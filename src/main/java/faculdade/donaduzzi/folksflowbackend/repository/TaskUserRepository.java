package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.stereotype.Repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.TaskUser;

@Repository
public interface TaskUserRepository extends BaseRepository<TaskUser, Integer> {
}

