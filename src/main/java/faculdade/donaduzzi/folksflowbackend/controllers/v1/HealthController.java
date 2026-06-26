package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Endpoint de saude (liveness/readiness) usado pelo healthcheck do Docker Compose.
 * Retorna 200 quando a aplicacao esta no ar e consegue falar com o banco,
 * e 503 caso o banco esteja indisponivel, garantindo que o container so
 * receba trafego quando estiver 100% pronto.
 */
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final DataSource dataSource;

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("application", "folks-flow-back-end");
        body.put("timestamp", Instant.now().toString());

        boolean databaseUp = isDatabaseReachable();
        body.put("database", databaseUp ? "UP" : "DOWN");

        if (databaseUp) {
            body.put("status", "UP");
            return ResponseEntity.ok(body);
        }

        body.put("status", "DOWN");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    private boolean isDatabaseReachable() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2);
        } catch (Exception e) {
            return false;
        }
    }
}
