package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.model.entities.Address;
import faculdade.donaduzzi.folksflowbackend.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressRepository repository;

    @PostMapping
    public ResponseEntity<Address> create(@RequestBody Address address) {
        return ResponseEntity.ok(repository.save(address));
    }
}
