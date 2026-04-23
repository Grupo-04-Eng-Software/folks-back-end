package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.stereotype.Repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.Candidate;

@Repository
public interface CandidateRepository extends BaseRepository<Candidate, Integer> {
}

