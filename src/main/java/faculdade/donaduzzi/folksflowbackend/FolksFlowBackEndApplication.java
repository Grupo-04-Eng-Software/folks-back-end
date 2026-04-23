package faculdade.donaduzzi.folksflowbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FolksFlowBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(FolksFlowBackEndApplication.class, args);

        System.out.println("\n=== FOLKS FLOW BACKEND INICIADO COM SUCESSO ===");
        System.out.println("Aplicação rodando em: http://localhost:8080");
        System.out.println("==============================================\n");
    }
}

