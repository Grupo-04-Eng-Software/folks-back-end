package faculdade.donaduzzi.folksflowbackend.repository.specifications;

import faculdade.donaduzzi.folksflowbackend.model.entities.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    private TaskSpecifications() {
    }

    public static Specification<Task> hasStatus(Integer statusId) {
        return (root, query, cb) -> statusId == null ? null : cb.equal(root.get("status").get("statusId"), statusId);
    }

    public static Specification<Task> hasPriority(Integer priorityId) {
        return (root, query, cb) -> priorityId == null ? null : cb.equal(root.get("priority").get("priorityId"), priorityId);
    }

    public static Specification<Task> hasTag(Integer tagId) {
        return (root, query, cb) -> tagId == null ? null : cb.equal(root.join("tags").get("tagId"), tagId);
    }

    public static Specification<Task> titleContains(String title) {
        return (root, query, cb) -> (title == null || title.isEmpty()) ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Task> hasAssignee(Integer userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.join("assignees").get("user").get("userId"), userId);
    }
}
