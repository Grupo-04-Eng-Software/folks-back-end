package faculdade.donaduzzi.folksflowbackend.repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.ChecklistItem;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChecklistItemRepository extends BaseRepository<ChecklistItem, Integer> {
    List<ChecklistItem> findByTaskTaskId(Integer taskId);
}
