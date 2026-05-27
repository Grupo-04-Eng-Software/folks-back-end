package faculdade.donaduzzi.folksflowbackend.repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends BaseRepository<Tag, Integer> {
    List<Tag> findBySpaceSpaceId(Integer spaceId);
}
