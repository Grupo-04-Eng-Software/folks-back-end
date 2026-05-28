package faculdade.donaduzzi.folksflowbackend.repository.specifications;

import faculdade.donaduzzi.folksflowbackend.model.entities.Candidate;
import org.springframework.data.jpa.domain.Specification;

public class CandidateSpecifications {

    public static Specification<Candidate> nameContains(String name) {
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Candidate> emailContains(String email) {
        return (root, query, cb) -> (email == null || email.isEmpty()) ? null : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Candidate> linkedInContains(String linkedIn) {
        return (root, query, cb) -> (linkedIn == null || linkedIn.isEmpty()) ? null : cb.like(cb.lower(root.get("linkedin")), "%" + linkedIn.toLowerCase() + "%");
    }
}
