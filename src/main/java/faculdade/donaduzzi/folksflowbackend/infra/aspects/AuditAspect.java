package faculdade.donaduzzi.folksflowbackend.infra.aspects;

import faculdade.donaduzzi.folksflowbackend.model.entities.AuditLog;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @Pointcut("execution(* faculdade.donaduzzi.folksflowbackend.services.*.create*(..))")
    public void createMethods() {}

    @Pointcut("execution(* faculdade.donaduzzi.folksflowbackend.services.*.update*(..))")
    public void updateMethods() {}

    @Pointcut("execution(* faculdade.donaduzzi.folksflowbackend.services.*.delete*(..))")
    public void deleteMethods() {}

    @Pointcut("execution(* faculdade.donaduzzi.folksflowbackend.services.*.move*(..))")
    public void moveMethods() {}

    @AfterReturning(pointcut = "createMethods() || updateMethods() || moveMethods()", returning = "result")
    public void auditSave(JoinPoint joinPoint, Object result) {
        saveLog(joinPoint, result, "SAVE");
    }

    @AfterReturning(pointcut = "deleteMethods()")
    public void auditDelete(JoinPoint joinPoint) {
        saveLog(joinPoint, null, "DELETE");
    }

    private void saveLog(JoinPoint joinPoint, Object result, String action) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof User user)) {
                return;
            }

            AuditLog log = new AuditLog();
            log.setUser(user);
            log.setAction(action);
            log.setCreatedAt(LocalDateTime.now());

            if (result != null) {
                log.setEntity(result.getClass().getSimpleName().replace("Response", ""));
                log.setEntityId(extractId(result));
                log.setNewValue(result.toString());
            } else {
                // Para delete, tentamos pegar o ID dos argumentos
                Object[] args = joinPoint.getArgs();
                if (args.length > 0 && args[0] instanceof Integer id) {
                    log.setEntityId(id);
                    log.setEntity(joinPoint.getTarget().getClass().getSimpleName().replace("Service", ""));
                }
            }

            auditLogRepository.save(log);
        } catch (Exception e) {
            log.error("Error saving audit log: {}", e.getMessage());
        }
    }

    private Integer extractId(Object obj) {
        try {
            // Tenta encontrar um método getId, getTaskId, getProjectId, etc.
            for (Method method : obj.getClass().getMethods()) {
                if ((method.getName().startsWith("get") && method.getName().endsWith("Id")) || method.getName().equals("getId")) {
                    Object id = method.invoke(obj);
                    if (id instanceof Integer) {
                        return (Integer) id;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
