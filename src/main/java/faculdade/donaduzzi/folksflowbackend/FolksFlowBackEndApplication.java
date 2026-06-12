package faculdade.donaduzzi.folksflowbackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class FolksFlowBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(FolksFlowBackEndApplication.class, args);
        log.info("Folks Flow backend started successfully at http://localhost:8080");
    }
}

