package faculdade.donaduzzi.folksflowbackend.repository;

import org.springframework.stereotype.Repository;

import faculdade.donaduzzi.folksflowbackend.model.entities.AuditLog;

@Repository
public interface AuditLogRepository extends BaseRepository<AuditLog, Integer> {
}


