package de.bayer.pharmacy.auditservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.auditservice.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {}
