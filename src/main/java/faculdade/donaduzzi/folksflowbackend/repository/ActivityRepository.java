package faculdade.donaduzzi.folksflowbackend.repository;


import org.springframework.stereotype.Repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.Activity;

import java.util.List;

@Repository
public interface ActivityRepository extends BaseRepository<Activity, Integer> {


    List<Activity> findActivitiesByTasktask_id(Integer taskTaskId);


}
