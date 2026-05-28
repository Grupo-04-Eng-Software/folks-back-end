package faculdade.donaduzzi.folksflowbackend.config;

import faculdade.donaduzzi.folksflowbackend.model.entities.Address;
import faculdade.donaduzzi.folksflowbackend.model.entities.Priority;
import faculdade.donaduzzi.folksflowbackend.model.entities.User;
import faculdade.donaduzzi.folksflowbackend.model.enums.UserRole;
import faculdade.donaduzzi.folksflowbackend.repository.AddressRepository;
import faculdade.donaduzzi.folksflowbackend.repository.PriorityRepository;
import faculdade.donaduzzi.folksflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PriorityRepository priorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (addressRepository.count() == 0) {
            seedDatabase();
        }
    }

    private void seedDatabase() {
        // 1. Criar Endereço Inicial
        Address address = new Address();
        address.setStreet("Rua Padrão Folks");
        address.setNumber("100");
        address.setNeighborhood("Centro");
        address.setCity("Toledo");
        address.setState("PR");
        address.setCountry("Brasil");
        address.setZipCode("85900-000");
        address = addressRepository.save(address);

        // 2. Criar Usuário Admin Inicial
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Administrador Folks");
            admin.setEmail("admin@folks.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            admin.setAddress(address);
            admin.setIsActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            userRepository.save(admin);
            System.out.println(">>> Usuário ADMIN criado: admin@folks.com / admin123");
        }

        // 3. Criar Prioridades Padrão
        if (priorityRepository.count() == 0) {
            createPriority("Baixa", 0);
            createPriority("Média", 1);
            createPriority("Alta", 2);
            createPriority("Urgente", 3);
            System.out.println(">>> Prioridades padrão criadas.");
        }
    }

    private void createPriority(String name, Integer position) {
        Priority priority = new Priority();
        priority.setName(name);
        priority.setPosition(position);
        priorityRepository.save(priority);
    }
}
