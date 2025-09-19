package de.bayer.pharmacy.edgeposapi.api;

import de.bayer.pharmacy.edgeposapi.domain.customer.Customer;
import de.bayer.pharmacy.edgeposapi.domain.customer.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final CustomerRepository repo;

    public PatientController(CustomerRepository r) {
        this.repo = r;
    }

    @PostMapping
    public Customer create(@RequestBody Customer p) {
        return repo.save(p);
    }

    @GetMapping
    public List<Customer> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Customer one(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }
}
